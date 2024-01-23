package com.gotcha.server.auth.controller;

import com.gotcha.server.auth.service.JwtTokenProvider;
import com.gotcha.server.auth.service.MemberDetailsService;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationResolver {
    private static final String BEARER = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDetailsService memberDetailsService;

    public Authentication resolveAuthentication(final String headerValue) {
        String token = resolveToken(headerValue);
        UserDetails userDetails = memberDetailsService.loadUserByUsername(jwtTokenProvider.getPayload(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(final String headerValue) {
        validateAuthorizationHeader(headerValue);
        String token = extractToken(headerValue);
        jwtTokenProvider.validateToken(token);
        return token;
    }

    private String extractToken(final String headerValue) {
        return headerValue.substring(BEARER.length()).trim();
    }

    private void validateAuthorizationHeader(final String headerValue) {
        if (!headerValue.toLowerCase().startsWith(BEARER.toLowerCase())) {
            throw new AppException(ErrorCode.INVALID_AUTHORIZATION_HEADER);
        }
    }
}
