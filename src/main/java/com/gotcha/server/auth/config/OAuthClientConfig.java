package com.gotcha.server.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OAuthClientConfig {
    @Bean
    public WebClient googleTokenWebClient() {
        ExchangeStrategies strategies = determineStrategies();
        return WebClient.builder()
                .baseUrl("https://oauth2.googleapis.com")
                .exchangeStrategies(strategies)
                .build();
    }

    @Bean
    public WebClient googleUserWebClient() {
        ExchangeStrategies strategies = determineStrategies();
        return WebClient.builder()
                .baseUrl("https://www.googleapis.com/oauth2")
                .exchangeStrategies(strategies)
                .build();
    }

    private ExchangeStrategies determineStrategies() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs()) // in-memory buffer의 기본 크기 256KB
                .build();
        strategies.messageWriters().stream()
                .filter(LoggingCodecSupport.class::isInstance)
                .forEach(writer -> ((LoggingCodecSupport)writer).setEnableLoggingRequestDetails(true));
        return strategies;
    }
}
