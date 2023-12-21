package com.gotcha.server.member.dto.response;

public record LoginResponse(Long userId, String accessToken, String refreshToken) {

}
