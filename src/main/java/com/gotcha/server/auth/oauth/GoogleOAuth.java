package com.gotcha.server.auth.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gotcha.server.auth.dto.GoogleTokenResponse;
import com.gotcha.server.auth.dto.GoogleUserResponse;
import com.gotcha.server.auth.dto.TokenInfoResponse;
import com.gotcha.server.auth.security.MemberDetailsService;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final MemberDetailsService memberDetailsService;

    public String getLoginUrl() {
        return GoogleUri.LOGIN.getUri(REDIRECT_URI, CLIENT_ID, DATA_SCOPE);
    }

    public GoogleTokenResponse requestAccessToken(String code) throws JsonProcessingException{
        Map<String, Object> params = GoogleUri.getTokenRequestParams(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, code);
        ResponseEntity<String> responseEntity=restTemplate.postForEntity(
                GoogleUri.TOKEN_REQUEST.getUri(), params, String.class);
        // Todo: responseEntity의 status code가 200이 아닐 때 예외 처리
       return objectMapper.readValue(responseEntity.getBody(), GoogleTokenResponse.class);
    }

    public GoogleUserResponse requestUserInfo(GoogleTokenResponse tokenResponse) throws JsonProcessingException {
        String url = GoogleUri.TOKEN_INFO_REQUEST.getUri("?id_token=", tokenResponse.id_token());
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        // Todo: responseEntity의 status code가 200이 아닐 때 예외 처리
        return objectMapper.readValue(responseEntity.getBody(), GoogleUserResponse.class);
    }

    public TokenInfoResponse requestTokenInfo(String accessToken) throws JsonProcessingException {
        String url = GoogleUri.TOKEN_INFO_REQUEST.getUri("?access_token=", accessToken);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if(responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        return objectMapper.readValue(responseEntity.getBody(), TokenInfoResponse.class);
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public Authentication getAuthentication(TokenInfoResponse tokenInfo) throws JsonProcessingException {
        UserDetails userDetails = memberDetailsService.loadUserByUsername(tokenInfo.socialId());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
