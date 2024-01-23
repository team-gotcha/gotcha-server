package com.gotcha.server.common;

import static com.gotcha.server.common.TestFixture.테스트유저;

import com.gotcha.server.auth.service.JwtTokenProvider;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TestTokenCreator {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public String create() {
        Member member = memberRepository.save(테스트유저("토큰을 위한 테스트유저"));
        return jwtTokenProvider.createAccessToken(member.getSocialId());
    }
}
