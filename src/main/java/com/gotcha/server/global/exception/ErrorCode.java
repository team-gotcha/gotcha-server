package com.gotcha.server.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "존재하지 않는 사용자입니다."),
    INVALID_TOKEN_REQUEST(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 요청 입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 access token 입니다."),
    INVALID_ID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 id token 입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 refresh token 입니다."),
    UNAUTHORIZED_INTERVIEWER(HttpStatus.UNAUTHORIZED, "면접관 권한이 없습니다."),

    NO_PARAMETER(HttpStatus.BAD_REQUEST, " 파라미터가 없습니다."),
    NAME_IS_EMPTY(HttpStatus.BAD_REQUEST, "면접 이름을 입력해주세요."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 주소입니다."),

    PROJECT_NOT_FOUNT(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트입니다."),
    APPLICANT_NOT_FOUNT(HttpStatus.NOT_FOUND, "존재하지 않는 지원자입니다."),
    INTERVIEW_NOT_FOUNT(HttpStatus.NOT_FOUND, "존재하지 않는 면접입니다."),
    QUESTION_NOT_FOUNT(HttpStatus.NOT_FOUND, "존재하지 않는 질문입니다."),
    INVALID_INTERVIEW_STATUS(HttpStatus.INTERNAL_SERVER_ERROR, "유효하지 않은 면접 단계입니다.");

    private HttpStatus httpStatus;
    private String message;
}
