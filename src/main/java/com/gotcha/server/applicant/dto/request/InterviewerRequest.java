package com.gotcha.server.applicant.dto.request;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.applicant.domain.Keyword;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InterviewerRequest {
    private Long id;
    private Applicant applicant;

    @Builder
    public InterviewerRequest(Long id, Applicant applicant) {
        this.id = id;
        this.applicant = applicant;
    }

    public Interviewer toEntity(Member member) {
        return Interviewer.builder()
                .applicant(applicant)
                .member(member)
                .build();
    }
}
