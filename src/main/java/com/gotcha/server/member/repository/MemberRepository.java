package com.gotcha.server.member.repository;

import java.util.Optional;

import com.gotcha.server.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialIdAndRefreshToken(String socialId, String refreshToken);

    Optional<Member> findBySocialId(String socialId);
}
