package com.gotcha.server.question.service;

import com.gotcha.server.question.domain.IndividualQuestion;
import java.util.function.BiConsumer;

public enum QuestionUpdateType {
    ORDER((question, value) -> question.updateOrder((Integer) value)),
    IMPORTANCE((question, value) -> question.updateImportance((Integer) value)),
    CONTENT((question, value) -> question.updateContent((String) value)),
    DELETE((question, value) -> question.deleteDuringInterview());

    private BiConsumer<IndividualQuestion, Object> expression;

    QuestionUpdateType(final BiConsumer<IndividualQuestion, Object> expression) {
        this.expression = expression;
    }

    public void update(final IndividualQuestion question, final Object value) {
        expression.accept(question, value);
    }
}
