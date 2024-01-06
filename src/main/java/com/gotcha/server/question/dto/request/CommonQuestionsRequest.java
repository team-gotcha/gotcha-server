package com.gotcha.server.question.dto.request;

import java.util.List;

public record CommonQuestionsRequest(List<String> questions, Long projectId) {

}
