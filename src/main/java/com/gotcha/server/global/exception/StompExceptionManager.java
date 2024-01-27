package com.gotcha.server.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class StompExceptionManager {
    @MessageExceptionHandler
    public ErrorDto handle(final Exception e) {
        log.info("StompExceptionManager error message: {}", e.getMessage());
        return new ErrorDto(determineErrorCode(e), e.getMessage());
    }

    private Integer determineErrorCode(final Exception e) {
        if(e instanceof AppException) {
            return ((AppException) e).getErrorCode().getHttpStatus().value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
