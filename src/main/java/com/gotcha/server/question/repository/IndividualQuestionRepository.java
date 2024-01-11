package com.gotcha.server.question.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.question.domain.IndividualQuestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndividualQuestionRepository extends JpaRepository<IndividualQuestion, Long>, QuestionDslRepository {
    List<IndividualQuestion> findAllByIdIn(List<Long> ids);
}
