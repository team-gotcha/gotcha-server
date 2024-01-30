package com.gotcha.server.auth.service;

import lombok.Getter;

@Getter
public enum CacheType {
    REFRESH_TOKEN("refreshToken", 86400, 10_000),
    USER("user", 86400, 10_000);

    private final String name;
    private final int expireTimeAfterWrite;
    private final int entryMaxSize;

    CacheType(final String name, final int expireTimeAfterWrite, final int entryMaxSize) {
        this.name = name;
        this.expireTimeAfterWrite = expireTimeAfterWrite;
        this.entryMaxSize = entryMaxSize;
    }
}
