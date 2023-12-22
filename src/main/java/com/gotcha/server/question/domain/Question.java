package com.gotcha.server.question.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class Question {
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer importance;

    @Column(nullable = false)
    private Integer questionOrder;
}
