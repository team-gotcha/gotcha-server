package com.gotcha.server.auth.dto;

public record GoogleUserResponse(String sub, String email, String name, String picture) {
    public String socialId() {
        return sub;
    }
}
