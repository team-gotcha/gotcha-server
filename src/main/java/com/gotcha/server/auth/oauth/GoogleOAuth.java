package com.gotcha.server.auth.oauth;

import com.gotcha.server.auth.dto.response.GoogleTokenResponse;
import com.gotcha.server.auth.dto.response.GoogleUserResponse;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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

    private final WebClient webClient;

    public GoogleTokenResponse requestTokens(String code) {
        Map<String, Object> params = GoogleUri.getTokenRequestParams(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, code);
        return webClient.post()
                .uri(GoogleUri.TOKEN_REQUEST.getUri())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(params)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> {
                    throw new AppException(ErrorCode.INVALID_GOOGLE_TOKEN_REQUEST);
                })
                .bodyToMono(GoogleTokenResponse.class)
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
}
