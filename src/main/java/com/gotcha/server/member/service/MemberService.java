package com.gotcha.server.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gotcha.server.auth.dto.RefreshTokenResponse;
import com.gotcha.server.auth.oauth.GoogleOAuth;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.member.domain.MemberRepository;
import com.gotcha.server.auth.dto.GoogleTokenResponse;
import com.gotcha.server.auth.dto.GoogleUserResponse;
import com.gotcha.server.member.dto.LoginResponse;
import com.gotcha.server.member.dto.RefreshTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final GoogleOAuth googleOAuth;

    public String getLoginUrl() {
        return googleOAuth.getLoginUrl();
    }

    public RefreshTokenResponse refresh(RefreshTokenRequest request) throws JsonProcessingException {
        Member member = memberRepository.findById(request.userId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return googleOAuth.requestRefresh(member.getRefreshToken());
    }

    @Transactional
    public LoginResponse login(String code) {
        GoogleTokenResponse googleToken = googleOAuth.requestTokens(code);
        GoogleUserResponse googleUser = googleOAuth.requestUserInfo(googleToken);
        Member member = memberRepository.findByEmail(googleUser.email()).orElse(null);
        if(member == null) {
            member = memberRepository.save(googleUser.toEntity(googleToken.refresh_token()));
        }
        return googleToken.toLoginResponse(member.getId());
    }

}
