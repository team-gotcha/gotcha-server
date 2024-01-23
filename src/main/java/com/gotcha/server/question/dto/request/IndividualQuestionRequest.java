package com.gotcha.server.question.dto.request;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.question.domain.IndividualQuestion;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class IndividualQuestionRequest {
    private String content;
    private Long applicantId;
    private Long commentTargetId;

    public IndividualQuestion toEntity(Member member, Applicant applicant, IndividualQuestion commentTarget){
        return IndividualQuestion.builder()
                .content(content)
                .applicant(applicant)
                .member(member)
                .commentTarget(commentTarget)
                .isCommon(false)
                .asking(false)
                .build();
    }
}
