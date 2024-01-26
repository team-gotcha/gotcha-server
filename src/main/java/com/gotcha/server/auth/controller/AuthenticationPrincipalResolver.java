package com.gotcha.server.auth.controller;

import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationPrincipalResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter, final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.isNull(authentication) || authentication instanceof AnonymousAuthenticationToken) {
            throw new AppException(ErrorCode.TOKEN_NOT_FOUND);
        }
        return authentication.getPrincipal();
    }
}
