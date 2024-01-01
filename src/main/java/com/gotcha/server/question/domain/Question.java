package com.gotcha.server.question.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class Question {
    @Column(nullable = false)
    protected String content;

    @Column(nullable = false)
    protected Integer importance;

    @Column(nullable = false)
    protected Integer questionOrder;
}
