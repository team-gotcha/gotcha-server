package com.gotcha.server.question.repository;

import com.gotcha.server.member.domain.Member;
import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.domain.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByQuestionAndMember(IndividualQuestion question, Member member);
}
