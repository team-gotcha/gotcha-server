package com.gotcha.server.evaluation.dto.response;

import java.util.List;

public record QuestionEvaluationResponse(String question, boolean isCommon, List<EvaluationResponse> evaluations) {
}
