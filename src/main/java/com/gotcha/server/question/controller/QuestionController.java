package com.gotcha.server.question.controller;

import com.gotcha.server.question.dto.request.AskingFlagsRequest;
import com.gotcha.server.question.dto.response.IndividualQuestionsResponse;
import com.gotcha.server.question.dto.response.QuestionRankResponse;
import com.gotcha.server.question.dto.request.IndividualQuestionRequest;
import com.gotcha.server.auth.dto.request.MemberDetails;
import com.gotcha.server.question.dto.request.CommonQuestionsRequest;
import com.gotcha.server.question.dto.response.InterviewQuestionResponse;
import com.gotcha.server.question.dto.response.PreparatoryQuestionResponse;
import com.gotcha.server.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/common")
    @Operation(description = "공통 질문을 생성한다.")
    public ResponseEntity<Void> createCommonQuestions(@RequestBody final CommonQuestionsRequest request) {
        questionService.createCommonQuestions(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(description = "면접 전 지원자의 개별 질문 목록을 조회한다.")
    public ResponseEntity<List<IndividualQuestionsResponse>> findAllIndividualQuestions(@RequestParam(name = "applicant-id") Long applicantId) {
        return ResponseEntity.ok(questionService.listIndividualQuestions(applicantId));
    }

    @GetMapping("/in-progress")
    @Operation(description = "지원자의 면접 중 질문 목록을 순서대로 조회한다.")
    public ResponseEntity<List<InterviewQuestionResponse>> findAllInterviewQuestions(@RequestParam(name = "applicant-id") Long applicantId) {
        return ResponseEntity.ok(questionService.listInterviewQuestions(applicantId));
    }

    @PostMapping("/individual")
    public ResponseEntity<String> createIndividualQuestion(
            @RequestBody @Valid IndividualQuestionRequest request,
            @AuthenticationPrincipal MemberDetails details) {
        questionService.createIndividualQuestion(request, details.member());
        return ResponseEntity.status(HttpStatus.CREATED).body("개별 질문이 입력되었습니다.");
    }

    @GetMapping("/preparatory")
    @Operation(description = "면접 전 질문 확인 창에서 질문 목록을 조회한다.")
    public ResponseEntity<List<PreparatoryQuestionResponse>> findAllPreparatoryQuestions(@RequestParam(value = "applicant-id") Long applicantId) {
        return ResponseEntity.ok(questionService.listPreparatoryQuestions(applicantId));
    }

    @GetMapping("/rank")
    @Operation(description = "면접 후, 지원자의 모든 면접 질문 id를 총점 순으로 조회한다.")
    public ResponseEntity<List<QuestionRankResponse>> findQuestionRanksByApplicant(@RequestParam(value = "applicant-id") Long applicantId) {
        return ResponseEntity.ok(questionService.findQuestionRanks(applicantId));
    }

    @PatchMapping("/asking-flags")
    @Operation(description = "지원자 상세 페이지의 댓글들의 '면접 때 질문하기' 값을 바꾼다.")
    public ResponseEntity<String> changeAskingFlags(@RequestBody @Valid AskingFlagsRequest request) {
        questionService.changeAskingFlags(request);
        return ResponseEntity.status(HttpStatus.OK).body("면접 때 질문하기 값이 업데이트 되었습니다.");
    }
}
