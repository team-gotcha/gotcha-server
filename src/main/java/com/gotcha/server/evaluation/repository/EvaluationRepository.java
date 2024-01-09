package com.gotcha.server.evaluation.repository;

import com.gotcha.server.evaluation.domain.Evaluation;
import com.gotcha.server.question.domain.IndividualQuestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findAllByQuestion(IndividualQuestion question);
}
