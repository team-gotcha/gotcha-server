package com.gotcha.server.question.dto.response;

import com.gotcha.server.question.domain.IndividualQuestion;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewQuestionResponse {
    private Long id;
    private Boolean isCommon;
    private String content;

    public static List<InterviewQuestionResponse> generateList(final List<IndividualQuestion> questions) {
        return questions.stream()
                .map(q -> InterviewQuestionResponse.builder()
                        .id(q.getId())
                        .isCommon(q.isCommon())
                        .content(q.getContent())
                        .build())
                .toList();
    }
}
