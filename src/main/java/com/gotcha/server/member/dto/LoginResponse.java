package com.gotcha.server.member.dto;

import com.gotcha.server.auth.dto.GoogleTokenResponse;
import com.gotcha.server.auth.dto.GoogleUserResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponse {
    private final Long userId;
    private final boolean isGuest;
    private final String accessToken;
    private final String refreshToken;
    private final String socialId;
    private final String email;
    private final String name;
    private final String profileUrl;

    public static LoginResponse from(
            final Long userId, final boolean isGuest,
            final GoogleTokenResponse googleToken, final GoogleUserResponse googleUser) {
        return new LoginResponse(
                userId, isGuest, googleToken.access_token(), googleToken.refresh_token(),
                googleUser.socialId(), googleUser.email(), googleUser.name(), googleUser.picture());
    }
}
