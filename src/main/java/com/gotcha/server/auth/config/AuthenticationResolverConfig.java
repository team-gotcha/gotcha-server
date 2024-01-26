package com.gotcha.server.auth.config;

import com.gotcha.server.auth.controller.AuthenticationPrincipalResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthenticationResolverConfig implements WebMvcConfigurer {
    private final AuthenticationPrincipalResolver authenticationPrincipalResolver;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationPrincipalResolver);
    }
}
