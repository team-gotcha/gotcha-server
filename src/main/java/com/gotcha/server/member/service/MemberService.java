package com.gotcha.server.member.service;

import com.gotcha.server.auth.service.JwtTokenProvider;
import com.gotcha.server.member.dto.request.EmailModifyRequest;
import com.gotcha.server.member.dto.response.TodayInterviewResponse;
import com.gotcha.server.applicant.repository.InterviewerRepository;
import com.gotcha.server.auth.oauth.GoogleOAuth;
import com.gotcha.server.auth.dto.request.MemberDetails;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.member.event.LoginEvent;
import com.gotcha.server.member.repository.MemberRepository;
import com.gotcha.server.auth.dto.response.GoogleTokenResponse;
import com.gotcha.server.auth.dto.response.GoogleUserResponse;
import com.gotcha.server.member.dto.response.LoginResponse;
import com.gotcha.server.project.domain.Collaborator;
import com.gotcha.server.project.repository.CollaboratorRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final InterviewerRepository interviewerRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final GoogleOAuth googleOAuth;
    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public LoginResponse login(String code, String redirectUri) {
        GoogleTokenResponse googleToken = googleOAuth.requestTokens(code, redirectUri);
        GoogleUserResponse googleUser = googleOAuth.requestUserInfo(googleToken);

        Member member = memberRepository.findBySocialId(googleUser.id()).orElse(null);
        if(Objects.isNull(member)) {
            member = memberRepository.save(googleUser.toEntity());
        }

        String accessToken = jwtTokenProvider.createAccessToken(member.getSocialId());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getSocialId());
        eventPublisher.publishEvent(new LoginEvent(member.getSocialId(), refreshToken));

        Long projectId = findFirstProjectId(member);

        return LoginResponse.builder()
                .userId(member.getId())
                .email(member.getEmail())
                .projectId(projectId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private Long findFirstProjectId(final Member member) {
        Collaborator collaborator = findFirstCollaborator(member);
        if(Objects.nonNull(collaborator)) {
            return collaborator.getProject().getId();
        }
        return null;
    }

    private Collaborator findFirstCollaborator(final Member member) {
        String email = member.getEmail();
        if(Objects.nonNull(email)) {
            return collaboratorRepository.findFirstByMember(email).orElse(null);
        }
        return null;
    }

    public TodayInterviewResponse countTodayInterview(final MemberDetails details) {
        long count = interviewerRepository.countTodayInterview(details.member());
        return new TodayInterviewResponse(count);
    }

    @Transactional
    public void modifyEmail(final MemberDetails details, final EmailModifyRequest request) {
        Member member = details.member();
        member.updateEmail(request.email());
        memberRepository.save(member);
    }
}
