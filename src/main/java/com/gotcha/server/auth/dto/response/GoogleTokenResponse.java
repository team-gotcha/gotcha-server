package com.gotcha.server.auth.dto.response;

public record GoogleTokenResponse(String access_token, Integer expires_in, String scope, String id_token) {

}
