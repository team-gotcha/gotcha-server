package com.gotcha.server.question.service;

import com.gotcha.server.question.domain.Question;
import java.util.function.BiConsumer;

public enum QuestionUpdateType {
    ORDER((question, value) -> question.updateOrder((Integer) value)),
    IMPORTANCE((question, value) -> question.updateImportance((Integer) value)),
    CONTENT((question, value) -> question.updateContent((String) value)),
    DELETE((question, value) -> question.deleteDuringInterview());

    private BiConsumer<Question, Object> expression;

    QuestionUpdateType(final BiConsumer<Question, Object> expression) {
        this.expression = expression;
    }

    public void update(final Question question, final Object value) {
        expression.accept(question, value);
    }
}
