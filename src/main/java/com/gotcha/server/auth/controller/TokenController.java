package com.gotcha.server.auth.controller;

import com.gotcha.server.auth.dto.response.RefreshTokenResponse;
import com.gotcha.server.auth.service.TokenService;
import com.gotcha.server.member.dto.request.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/api/refresh")
    @Operation(description = "access token을 재발급 받는다.")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @RequestBody final RefreshTokenRequest request) {
        return ResponseEntity.ok(tokenService.refresh(request));
    }
}
