package com.gotcha.server.question.dto.request;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Keyword;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.question.domain.IndividualQuestion;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class IndividualQuestionRequest {

    private String content;
    private Applicant applicant;
    private IndividualQuestion commentTarget;

    @Builder
    public IndividualQuestionRequest(String content, Applicant applicant, IndividualQuestion commentTarget) {
        this.content = content;
        this.applicant = applicant;
        this.commentTarget = commentTarget;
    }

    public IndividualQuestion toEntity(Member member){
        return IndividualQuestion.builder()
                .content(content)
                .applicant(applicant)
                .member(member)
                .commentTarget(commentTarget)
                .build();
    }
}
