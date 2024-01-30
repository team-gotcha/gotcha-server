package com.gotcha.server.member.repository;

import java.util.Optional;

import com.gotcha.server.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialId(String socialId);

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
