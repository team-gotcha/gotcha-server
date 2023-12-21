package com.gotcha.server.member.dto;

public record LoginResponse(Long userId, String accessToken, String refreshToken) {

}
