package com.gotcha.server.evaluation.controller;

import com.gotcha.server.auth.security.MemberDetails;
import com.gotcha.server.evaluation.dto.request.EvaluateRequest;
import com.gotcha.server.evaluation.dto.request.OneLinerRequest;
import com.gotcha.server.evaluation.dto.response.QuestionEvaluationResponse;
import com.gotcha.server.evaluation.dto.response.QuestionRankResponse;
import com.gotcha.server.evaluation.service.EvaluationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/evaluations")
@RequiredArgsConstructor
public class EvaluationController {
    private final EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<Void> evaluate(@AuthenticationPrincipal final MemberDetails details, @RequestBody final List<EvaluateRequest> requests) {
        evaluationService.evaluate(details, requests);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/one-liner")
    public ResponseEntity<Void> createOneLiner(@AuthenticationPrincipal final MemberDetails details, @RequestBody final OneLinerRequest request) {
        evaluationService.createOneLiner(details, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/questions")
    public ResponseEntity<QuestionEvaluationResponse> findEvaluationsByQuestion(@RequestParam(value = "question-id") Long questionId) {
        return ResponseEntity.ok(evaluationService.findQuestionEvaluations(questionId));
    }

    @GetMapping("/questions-rank")
    public ResponseEntity<List<QuestionRankResponse>> findQuestionRanksByApplicant(@RequestParam(value = "applicant-id") Long applicantId) {
        return ResponseEntity.ok(evaluationService.findQuestionRanks(applicantId));
    }
}
