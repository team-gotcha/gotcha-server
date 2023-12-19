package com.gotcha.server.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gotcha.server.auth.oauth.GoogleOAuth;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.member.domain.MemberRepository;
import com.gotcha.server.auth.dto.GoogleTokenResponse;
import com.gotcha.server.auth.dto.GoogleUserResponse;
import com.gotcha.server.member.dto.JoinRequest;
import com.gotcha.server.member.dto.LoginResponse;
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

    @Transactional
    public void join(JoinRequest request) {
        memberRepository.save(request.toEntity());
    }

    @Transactional
    public LoginResponse login(String code) throws JsonProcessingException {
        GoogleTokenResponse googleToken = googleOAuth.requestTokens(code);
        GoogleUserResponse googleUser = googleOAuth.requestUserInfo(googleToken);
        Member member = memberRepository.findByEmail(googleUser.email()).orElse(null);
        if(member != null) {
            return LoginResponse.from(member.getId(), false, googleToken, googleUser);
        }
        return LoginResponse.from(null, true, googleToken, googleUser);
    }

}
