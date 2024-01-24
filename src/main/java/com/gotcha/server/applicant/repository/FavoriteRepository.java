package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Favorite;
import com.gotcha.server.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByApplicantAndMember(Applicant applicant, Member member);
}
