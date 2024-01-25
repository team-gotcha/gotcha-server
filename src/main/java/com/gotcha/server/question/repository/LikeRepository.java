package com.gotcha.server.question.repository;

import com.gotcha.server.member.domain.Member;
import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.domain.Likes;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByQuestionAndMember(IndividualQuestion question, Member member);
    List<Likes> findAllByMemberAndQuestionIn(Member member, List<IndividualQuestion> questions);
}
