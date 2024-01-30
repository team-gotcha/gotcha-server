package com.gotcha.server.auth.controller;

import com.gotcha.server.auth.dto.request.MemberDetails;
import com.gotcha.server.auth.dto.response.RefreshTokenResponse;
import com.gotcha.server.auth.service.AuthService;
import com.gotcha.server.member.dto.request.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/api/refresh")
    @Operation(description = "access token을 재발급 받는다.")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @RequestBody final RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/api/logout")
    @Operation(description = "로그아웃한다.")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal final MemberDetails details) {
        authService.removeRefreshToken(details);
        return ResponseEntity.ok().build();
    }
}
