package com.gotcha.server.evaluation.controller;

import com.gotcha.server.auth.dto.request.MemberDetails;
import com.gotcha.server.evaluation.dto.request.EvaluateRequest;
import com.gotcha.server.evaluation.dto.request.OneLinerRequest;
import com.gotcha.server.evaluation.dto.response.QuestionEvaluationResponse;
import com.gotcha.server.evaluation.service.EvaluationService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(description = "면접 중, 사전 작성된 질문들에 코멘트와 점수(0~5점)를 기록한다.")
    public ResponseEntity<Void> evaluate(@AuthenticationPrincipal final MemberDetails details, @RequestBody final List<EvaluateRequest> requests) {
        evaluationService.evaluate(details, requests);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/one-liner")
    @Operation(description = "로그인 유저(면접관)가 지원자에게 한줄평을 작성한다.")
    public ResponseEntity<String> createOneLiner(@AuthenticationPrincipal final MemberDetails details, @RequestBody final OneLinerRequest request) {
        evaluationService.createOneLiner(details, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("한줄평이 입력되었습니다.");
    }

    @GetMapping("/questions")
    @Operation(description = "질문에 대해 모든 면접관의 평가를 조회한다.")
    public ResponseEntity<QuestionEvaluationResponse> findEvaluationsByQuestion(@RequestParam(value = "question-id") Long questionId) {
        return ResponseEntity.ok(evaluationService.findQuestionEvaluations(questionId));
    }
}
