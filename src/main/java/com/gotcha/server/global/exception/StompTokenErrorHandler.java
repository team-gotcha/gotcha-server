package com.gotcha.server.global.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompTokenErrorHandler extends StompSubProtocolErrorHandler {
    @Override
    @Nullable
    public Message<byte[]> handleClientMessageProcessingError(
            @Nullable Message<byte[]> clientMessage, Throwable ex) {
        Throwable exception = findException(ex);
        return super.handleClientMessageProcessingError(clientMessage, exception);
    }

    private Throwable findException(final Throwable exception) {
        if (exception instanceof MessageDeliveryException) {
            log.info("StompTokenErrorHandler error message: {}", exception.getCause().getMessage());
            return exception.getCause();
        }
        log.info("StompTokenErrorHandler error message: {}", exception.getMessage());
        return exception;
    }
}
