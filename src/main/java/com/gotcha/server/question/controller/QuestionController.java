package com.gotcha.server.question.controller;

import com.gotcha.server.member.domain.Member;
import com.gotcha.server.member.repository.MemberRepository;
import com.gotcha.server.question.dto.request.IndividualQuestionRequest;
import com.gotcha.server.auth.security.MemberDetails;
import com.gotcha.server.question.dto.request.CommonQuestionsRequest;
import com.gotcha.server.question.dto.response.InterviewQuestionResponse;
import com.gotcha.server.question.dto.response.PreparatoryQuestionResponse;
import com.gotcha.server.question.service.QuestionService;
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
    private final MemberRepository memberRepository;

    @PostMapping("/common")
    public ResponseEntity<Void> createCommonQuestions(final CommonQuestionsRequest request) {
        questionService.createCommonQuestions(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/in-progress/{applicant-id}")
    public ResponseEntity<List<InterviewQuestionResponse>> findAllInterviewQuestions(@PathVariable(name = "applicant-id") Long applicantId) {
        return ResponseEntity.ok(questionService.listInterviewQuestions(applicantId));
    }

    @PostMapping("/individual")
    public ResponseEntity<String> createIndividualQuestion(
            @RequestBody @Valid IndividualQuestionRequest request,
            @AuthenticationPrincipal MemberDetails details) {
////        테스트용 유저 생성
//        Member member = Member.builder()
//                .email("a@gmail.co")
//                .socialId("socialId")
//                .name("이름")
//                .profileUrl("a.jpg")
//                .refreshToken("token")
//                .build();
//        memberRepository.save(member);
//
//        questionService.createIndividualQuestion(request, member);
        questionService.createIndividualQuestion(request, details.member());
        return ResponseEntity.status(HttpStatus.CREATED).body("개별 질문이 입력되었습니다.");
    }

    @GetMapping("/preparatory")
    public ResponseEntity<List<PreparatoryQuestionResponse>> findAllPreparatoryQuestions(@RequestParam(value = "applicant-id") Long applicantId) {
        return ResponseEntity.ok(questionService.listPreparatoryQuestions(applicantId));
    }
}
