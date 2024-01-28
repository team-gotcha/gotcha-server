package com.gotcha.server.question.repository;

import com.gotcha.server.question.domain.IndividualQuestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IndividualQuestionRepository extends JpaRepository<IndividualQuestion, Long>, QuestionDslRepository {
    @Query("SELECT q FROM IndividualQuestion q "
            + "JOIN fetch q.applicant "
            + "WHERE q.id IN :ids")
    List<IndividualQuestion> findAllWithApplicantByIdIn(@Param(value = "ids") List<Long> ids);

    List<IndividualQuestion> findAllByIdIn(List<Long> ids);
}
