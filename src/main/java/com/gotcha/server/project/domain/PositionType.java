package com.gotcha.server.project.domain;

import lombok.Getter;

@Getter
public enum PositionType {
    MARKETING("마케팅/광고"),
    PLANNING("기획/전략"),
    FINANCE("재무/세무/IR/회계"),
    DESIGN("디자인/브랜딩"),
    PRODUCTION("생산/제조"),
    LOGISTICS("물류/유통"),
    MANAGEMENT("경영지원"),
    RND("연구개발/설계"),
    MUSIC("음악/댄스"),
    DEVELOPMENT("개발"),
    THEATER("연극/전시"),
    VIDEO("영상/영화"),
    FASHION("패션/뷰티");

    private final String value;

    PositionType(String value) {
        this.value = value;
    }
}
