package com.gotcha.server.auth.oauth;

import java.util.Map;
import java.util.stream.Collectors;

public enum GoogleUri {
    LOGIN("https://accounts.google.com/o/oauth2/v2/auth"),
    TOKEN_REQUEST("https://oauth2.googleapis.com/token"),
    TOKEN_INFO_REQUEST("https://oauth2.googleapis.com/tokeninfo"),
    REFRESH_TOKEN("https://oauth2.googleapis.com/token");

    private final String uri;

    GoogleUri(final String uri) {
        this.uri = uri;
    }

    /* LOGIN */
    public String getUri(final String redirectUri, final String clientId, final String scope) {
        Map<String,String> params = getLoginParams(redirectUri, clientId, scope);
        String paramsString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        return uri + "?" + paramsString;
    }

    private Map<String, String> getLoginParams(
            final String redirectUri, final String clientId, final String scope) {
        return Map.of(
                "redirect_uri", redirectUri,
                "client_id", clientId,
                "scope", scope,
                "access_type", "offline",
                "include_granted_scopes", "true",
                "state", "state_parameter_passthrough_value",
                "response_type", "code"
        );
    }

    /* TOKEN_REQUEST */
    public String getUri() {
        return uri;
    }

    public static Map<String, Object> getTokenRequestParams(
            final String clientId, final String clientSecret, final String redirectUri, final String code) {
        return Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "redirect_uri", redirectUri,
                "code", code,
                "grant_type", "authorization_code"
        );
    }

    /* TOKEN_INFO_REQUEST */
    public String getUri(final String tokenKey, final String token) {
        return uri + tokenKey + token;
    }

    /* REFRESH_TOKEN */
    public static Map<String, Object> getRefreshRequestParams(
            final String clientId, final String clientSecret, final String refreshToken) {
        return Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "refresh_token", refreshToken,
                "grant_type", "refresh_token"
        );
    }
}
