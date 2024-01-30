package com.gotcha.server.member.event;

public record LoginEvent(String socialId, String refreshToken) {

}
