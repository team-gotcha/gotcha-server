package com.gotcha.server.auth.dto.response;

import com.gotcha.server.member.dto.response.LoginResponse;

public record GoogleTokenResponse(
        String access_token, Integer expires_in, String refresh_token, String scope, String token_type, String id_token) {
    public LoginResponse toLoginResponse(Long userId) {
        return new LoginResponse(userId, access_token, refresh_token);
    }
}
