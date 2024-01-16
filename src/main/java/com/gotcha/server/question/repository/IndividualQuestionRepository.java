package com.gotcha.server.question.repository;

import com.gotcha.server.question.domain.IndividualQuestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IndividualQuestionRepository extends JpaRepository<IndividualQuestion, Long>, QuestionDslRepository {
    @Query("select q from IndividualQuestion q "
            + "join fetch q.applicant "
            + "where q.id in :ids")
    List<IndividualQuestion> findAllWithApplicantByIdIn(@Param(value = "ids") List<Long> ids);
}
