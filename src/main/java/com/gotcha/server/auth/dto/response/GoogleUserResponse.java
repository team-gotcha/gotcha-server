package com.gotcha.server.auth.dto.response;

import com.gotcha.server.member.domain.Member;

public record GoogleUserResponse(String id, String email, String name, String picture) {
    public Member toEntity() {
        return Member.builder()
                .socialId(id)
                .email(email)
                .name(name)
                .profileUrl(picture)
                .build();
    }
}
