package com.gotcha.server.auth.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.gotcha.server.auth.service.CacheType;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    public List<CaffeineCache> caffeineCaches() {
        return Arrays.stream(CacheType.values())
                .map(cacheType ->
                        new CaffeineCache(
                                cacheType.getName(),
                                Caffeine.newBuilder()
                                        .recordStats()
                                        .expireAfterWrite(cacheType.getExpireTimeAfterWrite(), TimeUnit.SECONDS)
                                        .maximumSize(cacheType.getEntryMaxSize())
                                        .build()))
                .toList();
    }

    @Bean
    public CacheManager cacheManager(final List<CaffeineCache> caffeineCaches) {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caffeineCaches);
        return cacheManager;
    }
}
