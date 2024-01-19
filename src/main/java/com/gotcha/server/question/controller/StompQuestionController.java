package com.gotcha.server.question.controller;

import com.gotcha.server.question.dto.message.QuestionUpdateMessage;
import com.gotcha.server.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompQuestionController {
    private final QuestionService questionService;

    @MessageMapping("/question/{question-id}")
    @SendTo("/sub/question/{question-id}")
    public QuestionUpdateMessage updateQuestion(
            @DestinationVariable(value = "question-id") Long questionId, @Payload QuestionUpdateMessage message) {
        questionService.updateQuestion(questionId, message);
        return message;
    }
}
