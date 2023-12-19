package com.gotcha.server.auth.dto;

public record RefreshTokenResponse(String access_token, int expires_in) {

}
