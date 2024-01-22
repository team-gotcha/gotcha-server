package com.gotcha.server.auth.oauth;

import java.util.Map;

public enum GoogleUri {
    TOKEN_REQUEST("token"),
    TOKEN_INFO_REQUEST("tokeninfo");

    private final String uri;

    GoogleUri(final String uri) {
        this.uri = uri;
    }

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
}
