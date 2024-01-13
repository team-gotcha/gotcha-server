package com.gotcha.server.evaluation.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.evaluation.domain.Evaluation;
import com.gotcha.server.evaluation.domain.OneLiner;
import com.gotcha.server.question.domain.IndividualQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface OneLinerRepository extends JpaRepository<OneLiner, Long>, OneLinerDslRepository{
}
