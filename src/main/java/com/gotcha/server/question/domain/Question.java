package com.gotcha.server.question.domain;

public interface Question {
    void updateOrder(final Integer order);
    void updateImportance(final Integer importance);
    void updateContent(final String content);
    void deleteDuringInterview();
}
