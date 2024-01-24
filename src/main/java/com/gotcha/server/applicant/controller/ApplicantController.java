package com.gotcha.server.applicant.controller;

import com.gotcha.server.applicant.dto.request.ApplicantRequest;
import com.gotcha.server.applicant.dto.request.GoQuestionPublicRequest;
import com.gotcha.server.applicant.dto.request.InterviewProceedRequest;
import com.gotcha.server.applicant.dto.request.PassEmailSendRequest;
import com.gotcha.server.applicant.dto.response.*;
import com.gotcha.server.applicant.service.ApplicantService;
import com.gotcha.server.auth.dto.request.MemberDetails;

import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/applicants")
@RequiredArgsConstructor
public class ApplicantController {
    private final ApplicantService applicantService;

    @PostMapping("/interview-ready")
    @Operation(description = "로그인 유저(면접관)가 면접 준비 완료를 요청한다. 모든 면접관이 준비 완료되었다면 지원자의 면접 단계가 바뀐다.")
    public ResponseEntity<InterviewProceedResponse> proceedToInterview(
            @RequestBody final InterviewProceedRequest request,
            @AuthenticationPrincipal final MemberDetails details) {
        return ResponseEntity.ok(applicantService.proceedToInterview(request, details));
    }

    @GetMapping
    @Operation(description = "세부 면접의 모든 지원자를 면접일 순으로 조회한다. 발표 완료된(ANNOUNCED) 지원자는 조회하지 않는다.")
    public ResponseEntity<List<ApplicantsResponse>> findAllApplicantByInterview(
            @RequestParam(name = "interview-id") final Long interviewId) {
        return ResponseEntity.ok(applicantService.listApplicantsByInterview(interviewId));
    }

    @GetMapping("/{applicant-id}")
    @Operation(description = "지원자의 상세 정보를 조회한다.")
    public ResponseEntity<ApplicantResponse> findApplicantDetailsById(
            @PathVariable(name = "applicant-id") final Long applicantId) {
        return ResponseEntity.ok(applicantService.findApplicantDetailsById(applicantId));
    }

    @PostMapping("/{applicant-id}/public")
    @Operation(description = "지원자의 면접 질문 공개를 동의 혹은 비동의한다.")
    public ResponseEntity<Void> makeQuestionsPublic(
            @PathVariable(name = "applicant-id") final Long applicantId,
            @RequestBody final GoQuestionPublicRequest request) {
        applicantService.makeQuestionsPublic(applicantId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pass")
    @Operation(description = "면접 진행 완료(COMPLETION) 지원자 중 합격한 지원자 목록을 조회한다.")
    public ResponseEntity<List<PassedApplicantsResponse>> findAllPassedApplicantsByInterview(
            @RequestParam(name = "interview-id") final Long interviewId) {
        return ResponseEntity.ok(applicantService.listPassedApplicantsByInterview(interviewId));
    }

    @PostMapping("/send-email")
    @Operation(description = "세부 면접 지원자들에게 합불 메일을 전송한다.")
    public ResponseEntity<Void> sendPassEmail(@RequestBody final PassEmailSendRequest request) {
        applicantService.sendPassEmail(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<String> createApplicant(
            @RequestBody final ApplicantRequest request,
            @AuthenticationPrincipal final MemberDetails details) {
        applicantService.createApplicant(request, details.member());
        return ResponseEntity.status(HttpStatus.CREATED).body("면접 지원자 정보가 입력되었습니다.");
    }

    @PostMapping("/files")
    public ResponseEntity<String> addApplicantFiles(
            @RequestParam(required = false) final MultipartFile resume,
            @RequestParam(required = false) final MultipartFile portfolio,
            @RequestParam(name = "applicant-id") final Long applicantId
    ) throws IOException {
        applicantService.addApplicantFiles(resume, portfolio, applicantId);
        return ResponseEntity.status(HttpStatus.CREATED).body("면접 지원자의 파일이 저장되었습니다.");
    }

    @GetMapping("/interview-completed")
    public ResponseEntity<List<CompletedApplicantsResponse>> getCompletedApplicants(
            @RequestParam(value = "interview-id") final Long interviewId) {
        return ResponseEntity.status(HttpStatus.OK).body(applicantService.getCompletedApplicants(interviewId));
    }

    @GetMapping("/interview-completed/details")
    public ResponseEntity<CompletedApplicantDetailsResponse> getCompletedApplicantDetails(
            @RequestParam(value = "applicant-id") final Long applicantId) {
        return ResponseEntity.status(HttpStatus.OK).body(applicantService.getCompletedApplicantDetails(applicantId));
    }
}
