package com.gotcha.server.auth.service;

import com.gotcha.server.auth.dto.request.MemberDetails;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Cacheable(value = "user")
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findBySocialId(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return new MemberDetails(member);
    }
}
