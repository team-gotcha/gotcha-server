package com.gotcha.server.question.dto.message;

import com.gotcha.server.question.service.QuestionUpdateType;

public record QuestionUpdateMessage(Object value, QuestionUpdateType type) {

}
