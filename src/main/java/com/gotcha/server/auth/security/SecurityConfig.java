package com.gotcha.server.auth.security;

import com.gotcha.server.auth.oauth.GoogleAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final GoogleAuthenticationFilter googleAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)

                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
//                        .requestMatchers("/post").authenticated()
//                        .requestMatchers(HttpMethod.DELETE, "/post/*").authenticated()
                        .anyRequest().permitAll()
                );

        http.addFilterBefore(googleAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
