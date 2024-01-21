package com.gotcha.server.question.dto.response;

import com.gotcha.server.question.domain.IndividualQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreparatoryQuestionResponse {
    private Long id;
    private boolean isCommon;
    private int questionOrder;
    private String content;
    private int importance;

    public static PreparatoryQuestionResponse from(final IndividualQuestion question) {
        return PreparatoryQuestionResponse.builder()
                .id(question.getId())
                .isCommon(question.isCommon())
                .questionOrder(question.getQuestionOrder())
                .content(question.getContent())
                .importance(question.getImportance())
                .build();
    }
}
