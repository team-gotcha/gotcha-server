package com.gotcha.server.project.domain;

import lombok.Getter;

@Getter
public enum AreaType {
    SERVICE("서비스"),
    FINANCE("금융/은행"),
    IT("IT/테크"),
    MANUFACTURE("제조/생산/화학"),
    EDUCATION("교육"),
    CONSTRUCTION("건설"),
    MEDICAL("의료/제약"),
    MEDIA("미디어"),
    ACADEMIC("학술"),
    DISTRIBUTION("유통/F&B"),
    CULTURE("문화/예술/디자인"),
    VOLUNTEERING("봉사"),
    FASHION("패션/뷰티");

    private final String value;

    AreaType(String value) {
        this.value = value;
    }
}
