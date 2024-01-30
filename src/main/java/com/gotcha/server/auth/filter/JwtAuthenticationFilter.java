package com.gotcha.server.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gotcha.server.auth.controller.AuthorizationResolver;
import com.gotcha.server.global.exception.ErrorDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthorizationResolver authorizationResolver;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            String authorization = extractAuthorizationHeader(request);
            if (Objects.nonNull(authorization)) {
                Authentication authentication = authorizationResolver.resolveAuthentication(authorization);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            setErrorResponse(response, e.getMessage());
        }
    }

    private String extractAuthorizationHeader(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    private void setErrorResponse(HttpServletResponse response, String errorMessage){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ErrorDto errorResponse = new ErrorDto(HttpStatus.UNAUTHORIZED.value(), errorMessage);
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
