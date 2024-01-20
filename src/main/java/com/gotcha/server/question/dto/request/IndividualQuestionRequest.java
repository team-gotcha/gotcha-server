package com.gotcha.server.question.dto.request;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Keyword;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.applicant.repository.ApplicantRepository;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.repository.IndividualQuestionRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class IndividualQuestionRequest {

    private String content;
    private Long applicantId;
    private Long commentTargetId;

    @Builder
    public IndividualQuestionRequest(String content, Long applicantId, Long commentTargetId) {
        this.content = content;
        this.applicantId = applicantId;
        this.commentTargetId = commentTargetId;
    }

    public IndividualQuestion toEntity(Member member, ApplicantRepository applicantRepository, IndividualQuestionRepository individualQuestionRepository){
        Applicant applicant = null;
        if (applicantId != null) {
            applicant = applicantRepository.findById(applicantId)
                    .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        }

        IndividualQuestion commentTarget = null;
        if (commentTargetId != null) {
            commentTarget = individualQuestionRepository.findById(commentTargetId)
                    .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUNT));
        }

        return IndividualQuestion.builder()
                .content(content)
                .applicant(applicant)
                .member(member)
                .commentTarget(commentTarget)
                .build();
    }
}
