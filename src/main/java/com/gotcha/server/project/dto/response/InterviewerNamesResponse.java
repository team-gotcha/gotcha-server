package com.gotcha.server.project.dto.response;

import com.gotcha.server.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InterviewerNamesResponse {

    private Long id;
    private String email;
    private String name;

    @Builder
    public InterviewerNamesResponse(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static InterviewerNamesResponse from(Member member){
        return InterviewerNamesResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }

}
