package com.gotcha.server.question.event;

import com.gotcha.server.mongo.domain.QuestionMongo;
import java.util.List;

public record QuestionUpdatedEvent(List<QuestionMongo> mongoQuestions) {

}
