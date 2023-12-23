package com.gotcha.server.project.domain;

import lombok.Getter;

@Getter
public enum LayoutType {
    REGISTERED("지원자 등록 상태"),
    IN_PROGRESS("진행 상태");

    private final String value;

    LayoutType(String value) {
        this.value = value;
    }
}

