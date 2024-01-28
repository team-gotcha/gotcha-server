package com.gotcha.server.question.event;

import com.gotcha.server.question.domain.IndividualQuestion;
import java.util.List;

public record QuestionPreparedEvent(List<IndividualQuestion> individualQuestions, Long applicantId) {

}
