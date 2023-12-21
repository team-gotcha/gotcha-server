package com.gotcha.server.auth.oauth;

import com.gotcha.server.auth.dto.GoogleTokenResponse;
import com.gotcha.server.auth.dto.GoogleUserResponse;
import com.gotcha.server.auth.dto.RefreshTokenResponse;
import com.gotcha.server.auth.dto.TokenInfoResponse;
import com.gotcha.server.auth.security.MemberDetailsService;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class GoogleOAuth {
    @Value("${oauth.google.redirect-url}")
    private String REDIRECT_URI;

    @Value("${oauth.google.client-id}")
    private String CLIENT_ID;

    @Value("${oauth.google.client-secret}")
    private String CLIENT_SECRET;

    @Value("${oauth.google.scope}")
    private String DATA_SCOPE;

    private final WebClient webClient;
    private final MemberDetailsService memberDetailsService;

    public String getLoginUrl() {
        return GoogleUri.LOGIN.getUri(REDIRECT_URI, CLIENT_ID, DATA_SCOPE);
    }

    public GoogleTokenResponse requestTokens(String code) {
        Map<String, Object> params = GoogleUri.getTokenRequestParams(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, code);
        return webClient.post()
                .uri(GoogleUri.TOKEN_REQUEST.getUri())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(params)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> {
                    throw new AppException(ErrorCode.INVALID_TOKEN_REQUEST);
                })
                .bodyToMono(GoogleTokenResponse.class)
                .block();
    }

    public RefreshTokenResponse requestRefresh(String refreshToken) {
        Map<String, Object> params = GoogleUri.getRefreshRequestParams(CLIENT_ID, CLIENT_SECRET, refreshToken);
        return webClient.post()
                .uri(GoogleUri.TOKEN_REQUEST.getUri()).accept(MediaType.APPLICATION_JSON)
                .bodyValue(params)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> {
                    throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
                })
                .bodyToMono(RefreshTokenResponse.class)
                .block();
    }

    public GoogleUserResponse requestUserInfo(GoogleTokenResponse tokenResponse) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(GoogleUri.TOKEN_INFO_REQUEST.getUri())
                        .queryParam("id_token", tokenResponse.id_token())
                        .build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> {
                    throw new AppException(ErrorCode.INVALID_ID_TOKEN);
                })
                .bodyToMono(GoogleUserResponse.class)
                .block();
    }

    public TokenInfoResponse requestTokenInfo(String accessToken) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(GoogleUri.TOKEN_INFO_REQUEST.getUri())
                        .queryParam("access_token", accessToken)
                        .build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> {
                    throw new AppException(ErrorCode.INVALID_ACCESS_TOKEN);
                })
                .bodyToMono(TokenInfoResponse.class)
                .block();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public Authentication getAuthentication(TokenInfoResponse tokenInfo) {
        UserDetails userDetails = memberDetailsService.loadUserByUsername(tokenInfo.socialId());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
