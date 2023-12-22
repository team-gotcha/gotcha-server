package com.gotcha.server.question.repository;

import com.gotcha.server.question.domain.IndividualQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndividualQuestionRepository extends JpaRepository<IndividualQuestion, Long> {

}
