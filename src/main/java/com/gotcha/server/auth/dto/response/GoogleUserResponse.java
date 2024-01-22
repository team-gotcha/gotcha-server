package com.gotcha.server.auth.dto.response;

import com.gotcha.server.member.domain.Member;

public record GoogleUserResponse(String sub, String email, String name, String picture) {
    public Member toEntity() {
        return Member.builder()
                .socialId(sub)
                .email(email)
                .name(name)
                .profileUrl(picture)
                .build();
    }
}
