package com.gotcha.server.auth.dto.response;

public record TokenInfoResponse(String sub, String email) {
    public String socialId() {
        return sub;
    }
}
