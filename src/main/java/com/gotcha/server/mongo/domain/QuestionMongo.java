package com.gotcha.server.mongo.domain;

import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.domain.Question;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
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
    private Boolean asking;

    @Builder
    public QuestionMongo(
            final Long questionId, final String content,
            final Integer importance, final Integer questionOrder,
            final Boolean isCommon, final Long applicantId) {
        this.questionId = questionId;
        this.content = content;
        this.importance = importance;
        this.questionOrder = questionOrder;
        this.isCommon = isCommon;
        this.applicantId = applicantId;
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

    public boolean isCommon() {
        return this.isCommon;
    }

    public static QuestionMongo from(final IndividualQuestion individualQuestion, final Long applicantId) {
        return QuestionMongo.builder()
                .questionId(individualQuestion.getId())
                .content(individualQuestion.getContent())
                .importance(individualQuestion.getImportance())
                .questionOrder(individualQuestion.getQuestionOrder())
                .isCommon(individualQuestion.isCommon())
                .applicantId(applicantId)
                .build();
    }

    public IndividualQuestion updateQuestion(final IndividualQuestion question) {
        question.updateOrder(questionOrder);
        question.updateImportance(importance);
        question.updateContent(content);
        if(!asking) {
            question.deleteDuringInterview();
        }
        return question;
    }
}
