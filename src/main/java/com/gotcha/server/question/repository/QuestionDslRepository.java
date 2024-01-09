package com.gotcha.server.question.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.question.domain.IndividualQuestion;
import java.util.List;

public interface QuestionDslRepository {
    List<IndividualQuestion> findAllDuringInterview(Applicant applicant);
}
