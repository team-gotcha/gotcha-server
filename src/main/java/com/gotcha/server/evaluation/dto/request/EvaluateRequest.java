package com.gotcha.server.evaluation.dto.request;

public record EvaluateRequest (Long questionId, Integer score, String content){

}
