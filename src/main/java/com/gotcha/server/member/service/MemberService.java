package com.gotcha.server.member.service;

import com.gotcha.server.member.dto.response.TodayInterviewResponse;
import com.gotcha.server.applicant.repository.InterviewerRepository;
import com.gotcha.server.auth.dto.response.RefreshTokenResponse;
import com.gotcha.server.auth.oauth.GoogleOAuth;
import com.gotcha.server.auth.security.MemberDetails;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.member.repository.MemberRepository;
import com.gotcha.server.auth.dto.response.GoogleTokenResponse;
import com.gotcha.server.auth.dto.response.GoogleUserResponse;
import com.gotcha.server.member.dto.response.LoginResponse;
import com.gotcha.server.member.dto.request.RefreshTokenRequest;
import com.gotcha.server.member.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final InterviewerRepository interviewerRepository;
    private final GoogleOAuth googleOAuth;

    public String getLoginUrl() {
        return googleOAuth.getLoginUrl();
    }

    public RefreshTokenResponse refresh(RefreshTokenRequest request) {
        Member member = memberRepository.findById(request.userId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return googleOAuth.requestRefresh(member.getRefreshToken());
    }

    @Transactional
    public LoginResponse login(String code) {
        GoogleTokenResponse googleToken = googleOAuth.requestTokens(code);
        GoogleUserResponse googleUser = googleOAuth.requestUserInfo(googleToken);
        Member member = memberRepository.findBySocialId(googleUser.sub()).orElse(null);
        if(member == null) {
            member = memberRepository.save(googleUser.toEntity(googleToken.refresh_token()));
        }
        return googleToken.toLoginResponse(member.getId());
    }

    public UserResponse getUserDetails(final MemberDetails details) {
        Member member = details.member();
        return new UserResponse(member.getProfileUrl(), member.getName(), member.getEmail());
    }

    public TodayInterviewResponse countTodayInterview(final MemberDetails details) {
        long count = interviewerRepository.countTodayInterview(details.member());
        return new TodayInterviewResponse(count);
    }
}
