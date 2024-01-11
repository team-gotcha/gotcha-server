package com.gotcha.server.applicant.domain;

public enum Outcome {
    PENDING(""),
    PASS("합격하신 것을 축하드립니다.\n담당자가 빠른 시일 내 개별적으로 연락을 드릴 예정입니다.\n"),
    FAIL("아쉽게도 불합격하셨습니다.\n");

    String message;

    Outcome(final String message) {
        this.message = message;
    }

    public String createPassEmailMessage(
            final String name, final String project, final String interview, final String position) {
        return String.format("안녕하세요. %s님.\n", name)
                + String.format("귀하께서 지원하신 %s %s %s 전형에 ", project, interview, position)
                + this.message
                + String.format("이번 %s %s %s 전형에 대한 관심과 지원 감사합니다.", project, interview, position);
    }
}
