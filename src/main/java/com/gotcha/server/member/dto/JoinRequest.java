package com.gotcha.server.member.dto;

import com.gotcha.server.member.domain.Member;

public record JoinRequest(
        String socialId, String email, String name, String profileUrl, String refreshToken) {
    public Member toEntity() {
        return Member.builder()
                .socialId(socialId)
                .email(email)
                .name(name)
                .profileUrl(profileUrl)
                .refreshToken(refreshToken)
                .build();
    }
}
