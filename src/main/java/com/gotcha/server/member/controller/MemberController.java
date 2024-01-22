package com.gotcha.server.member.controller;

import com.gotcha.server.member.dto.response.TodayInterviewResponse;
import com.gotcha.server.auth.dto.response.RefreshTokenResponse;
import com.gotcha.server.auth.dto.request.MemberDetails;
import com.gotcha.server.member.dto.response.LoginResponse;
import com.gotcha.server.member.dto.request.RefreshTokenRequest;
import com.gotcha.server.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
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

    @GetMapping("/api/login/google")
    @Operation(description = "access token과 refresh token을 발급 받는다. 회원가입 되지 않은 유저라면 가입한다.")
    public ResponseEntity<LoginResponse> getGoogleToken(@RequestParam String code) {
        return ResponseEntity.ok(memberService.login(code));
    }

    @PostMapping("/api/refresh")
    @Operation(description = "access token을 재발급 받는다.")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(memberService.refresh(request));
    }

    @GetMapping("/api/todays-interview")
    @Operation(description = "로그인한 유저의 오늘 예정된 면접 수를 조회한다.")
    public ResponseEntity<TodayInterviewResponse> countInterview(@AuthenticationPrincipal final MemberDetails details) {
        return ResponseEntity.ok(memberService.countTodayInterview(details));
    }

    @PostMapping("/api/logout")
    @Operation(description = "로그아웃한다.")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal final MemberDetails details) {
        memberService.logout(details);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Hello World");
    }
}
