package com.gotcha.server.member.controller;

import com.gotcha.server.auth.dto.RefreshTokenResponse;
import com.gotcha.server.auth.security.MemberDetails;
import com.gotcha.server.member.dto.LoginResponse;
import com.gotcha.server.member.dto.RefreshTokenRequest;
import com.gotcha.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/auth/login/google")
    public ResponseEntity<String> redirectGoogleLogin(){
        return ResponseEntity.ok(memberService.getLoginUrl());
    }

    @GetMapping("/auth/callback/google")
    public ResponseEntity<LoginResponse> callbackGoogleLogin(@RequestParam String code) throws Exception {
        return ResponseEntity.ok(memberService.login(code));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) throws Exception {
        return ResponseEntity.ok(memberService.refresh(request));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(@AuthenticationPrincipal MemberDetails details) {
        return ResponseEntity.ok(details.member().getEmail());
    }
}
