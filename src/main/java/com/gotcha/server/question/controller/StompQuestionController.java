package com.gotcha.server.question.controller;

import com.gotcha.server.global.aop.ExeTimer;
import com.gotcha.server.question.dto.message.QuestionUpdateMessage;
import com.gotcha.server.question.service.QuestionMongoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompQuestionController {
    private final QuestionMongoService questionService;

    @MessageMapping("/question/{question-id}")
    @SendTo("/sub/question/{question-id}")
    @Operation(description = "실시간으로 질문을 수정 및 삭제한다.")
    @ExeTimer
    public QuestionUpdateMessage updateQuestion(
            @DestinationVariable(value = "question-id") Long questionId, @Payload QuestionUpdateMessage message) {
        questionService.updateQuestion(questionId, message);
        return message;
    }
}
