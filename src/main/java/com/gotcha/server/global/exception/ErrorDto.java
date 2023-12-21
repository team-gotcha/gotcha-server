package com.gotcha.server.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDto {

    private String errorCode;
    private String message;

    public ErrorDto(AppException e) {
        this.errorCode = e.getErrorCode().toString();
        this.message = e.getErrorCode().getMessage();
    }

    public ErrorDto(ErrorCode errorCode) {
        this.errorCode = errorCode.toString();
        this.message = errorCode.getMessage();
    }

}