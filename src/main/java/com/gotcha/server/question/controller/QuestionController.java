package com.gotcha.server.question.controller;

import com.gotcha.server.question.dto.request.CommonQuestionsRequest;
import com.gotcha.server.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/common")
    public ResponseEntity<Void> createCommonQuestions(final CommonQuestionsRequest request) {
        questionService.createCommonQuestions(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
