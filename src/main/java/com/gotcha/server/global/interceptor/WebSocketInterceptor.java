package com.gotcha.server.global.interceptor;

import com.gotcha.server.auth.controller.AuthorizationResolver;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {
    private final AuthorizationResolver authorizationResolver;

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String headerValue = extractAuthorizationHeader(accessor);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            authorizationResolver.resolveToken(headerValue);
        }
        return message;
    }

    private String extractAuthorizationHeader(final StompHeaderAccessor accessor) {
        List<String> values = accessor.getNativeHeader(StompHeaderAccessor.STOMP_LOGIN_HEADER);
        if(Objects.isNull(values) || values.size() == 0) {
            throw new AppException(ErrorCode.TOKEN_NOT_FOUND);
        }
        return values.get(0);
    }
}
