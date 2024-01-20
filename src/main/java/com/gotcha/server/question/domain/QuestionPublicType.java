package com.gotcha.server.question.domain;

public enum QuestionPublicType {
    PENDING,
    PRIVATE,
    PUBLIC;

    public QuestionPublicType change(final boolean agree) {
        if(!agree || this.equals(PRIVATE)) {
            return PRIVATE;
        }
        return PUBLIC;
    }
}
