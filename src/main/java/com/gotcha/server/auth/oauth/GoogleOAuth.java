package com.gotcha.server.auth.oauth;

import com.gotcha.server.auth.dto.response.GoogleTokenResponse;
import com.gotcha.server.auth.dto.response.GoogleUserResponse;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GoogleOAuth {
    private final String clientId;
    private final String clientSecret;
    private final WebClient googleTokenWebClient;
    private final WebClient googleUserWebClient;

    public GoogleOAuth(
            @Value("${oauth.google.client-id}") final String clientId,
            @Value("${oauth.google.client-secret}") final String clientSecret,
            final WebClient googleTokenWebClient, final WebClient googleUserWebClient) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.googleTokenWebClient = googleTokenWebClient;
        this.googleUserWebClient = googleUserWebClient;
    }

    public GoogleTokenResponse requestTokens(String code, String redirectUri) {
        Map<String, Object> params = GoogleUri.getTokenRequestParams(clientId, clientSecret, redirectUri, code);
        return googleTokenWebClient.post()
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
        return googleUserWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(GoogleUri.USER_INFO_REQUEST.getUri())
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.access_token())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> {
                    throw new AppException(ErrorCode.INVALID_ID_TOKEN);
                })
                .bodyToMono(GoogleUserResponse.class)
                .block();
    }
}
