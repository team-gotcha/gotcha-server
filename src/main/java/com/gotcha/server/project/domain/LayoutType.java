package com.gotcha.server.project.domain;

import lombok.Getter;

@Getter
public enum LayoutType {
    LIST("목록으로 정렬하기"),
    BOARD("보드");

    private final String value;

    LayoutType(String value) {
        this.value = value;
    }
}

