package com.gotcha.server.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "로그인한 사용자가 존재하지 않습니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    INVALID_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "유효하지 않은 Authorization 헤더 형식입니다."),
    INVALID_GOOGLE_TOKEN_REQUEST(HttpStatus.UNAUTHORIZED, "유효하지 않은 구글 토큰 요청입니다."),
    MALFORMED_JWT(HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "JWT 토큰이 잘못되었습니다."),
    INVALID_ID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 구글 id 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 refresh token 입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 access token 입니다."),
    UNAUTHORIZED_INTERVIEWER(HttpStatus.UNAUTHORIZED, "면접관 권한이 없습니다."),

    NO_PARAMETER(HttpStatus.BAD_REQUEST, "%s 파라미터가 없습니다."),
    NAME_IS_EMPTY(HttpStatus.BAD_REQUEST, "면접 이름을 입력해주세요."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 주소입니다."),
    INVALID_GOOGLE_EMAIL(HttpStatus.BAD_REQUEST, "구글 이메일 주소가 아닙니다."),
    INVALID_IMPORTANCE(HttpStatus.BAD_REQUEST, "중요도의 범위는 1~5입니다."),
    CONTENT_IS_EMPTY(HttpStatus.BAD_REQUEST, "내용을 입력해주세요."),
    MULTIPLE_APPLICANT_EVALUATION(HttpStatus.BAD_REQUEST, "동일하지 않은 지원자에 대한 평가 요청입니다.."),
    DUPLICATE_INTERVIEWER(HttpStatus.BAD_REQUEST, "면접관이 중복되었습니다."),

    PROJECT_NOT_FOUNT(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트입니다."),
    APPLICANT_NOT_FOUNT(HttpStatus.NOT_FOUND, "존재하지 않는 지원자입니다."),
    INTERVIEW_NOT_FOUNT(HttpStatus.NOT_FOUND, "존재하지 않는 면접입니다."),
    QUESTION_NOT_FOUNT(HttpStatus.NOT_FOUND, "존재하지 않는 질문입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 면접관입니다."),
  
    INVALID_INTERVIEW_STATUS(HttpStatus.INTERNAL_SERVER_ERROR, "유효하지 않은 면접 단계입니다."),
    UNABLE_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이메일을 보낼 수 없습니다.");
    private HttpStatus httpStatus;
    private String message;
}
