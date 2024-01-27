package com.gotcha.server.mongo.domain;

import com.gotcha.server.question.domain.Question;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "questions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionMongo implements Question {
    @Id
    private String id;
    private Long questionId;
    private String content;
    private Integer importance;
    private Integer questionOrder;
    private Boolean isCommon;
    private Long applicantId;
    private Long writerId;
    private Boolean asking;

    @Builder
    public QuestionMongo(
            final Long questionId, final String content,
            final Integer importance, final Integer questionOrder,
            final Boolean isCommon, final Long applicantId, final Long writerId) {
        this.questionId = questionId;
        this.content = content;
        this.importance = importance;
        this.questionOrder = questionOrder;
        this.isCommon = isCommon;
        this.applicantId = applicantId;
        this.writerId = writerId;
        this.asking = true;
    }

    public void updateOrder(final Integer order) {
        this.questionOrder = order;
    }

    public void updateImportance(final Integer importance) {
        this.importance = importance;
    }

    public void updateContent(final String content) {
        this.content = content;
    }

    public void deleteDuringInterview() {
        this.asking = false;
    }
}
