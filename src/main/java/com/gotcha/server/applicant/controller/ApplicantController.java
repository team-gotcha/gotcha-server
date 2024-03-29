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

    @PostMapping
    @Operation(description = "지원자 정보를 생성한다.")
    public ResponseEntity<ApplicantIdResponse> createApplicant(@RequestBody final ApplicantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicantService.createApplicant(request));
    }

    @PatchMapping("/files")
    @Operation(description = "지원자 정보 페이지에 파일을 추가한다.")
    public ResponseEntity<String> addApplicantFiles(
            @RequestParam(required = false) final MultipartFile resume,
            @RequestParam(required = false) final MultipartFile portfolio,
            @RequestParam(name = "applicant-id") final Long applicantId
    ) throws IOException {
        applicantService.addApplicantFiles(resume, portfolio, applicantId);
        return ResponseEntity.status(HttpStatus.CREATED).body("면접 지원자의 파일이 저장되었습니다.");
    }

    @GetMapping("/{applicant-id}")
    @Operation(description = "지원자의 상세 정보를 조회한다.")
    public ResponseEntity<ApplicantResponse> findApplicantDetailsById(
            @PathVariable(name = "applicant-id") final Long applicantId) {
        return ResponseEntity.ok(applicantService.findApplicantDetailsById(applicantId));
    }

    @GetMapping
    @Operation(description = "세부 면접의 모든 지원자를 면접일 순으로 조회한다. 발표 완료된(ANNOUNCED) 지원자는 조회하지 않는다.")
    public ResponseEntity<List<ApplicantsResponse>> findAllApplicantByInterview(
            @RequestParam(name = "interview-id") final Long interviewId,
            @AuthenticationPrincipal final MemberDetails details) {
        return ResponseEntity.ok(applicantService.findAllApplicantByInterview(interviewId, details));
    }

    @PostMapping("/{applicant-id}/favorite")
    @Operation(description = "지원자를 즐겨찾기 하거나 취소한다.")
    public ResponseEntity<Void> updateFavorite(
            @PathVariable(name = "applicant-id") final Long applicantId,
            @AuthenticationPrincipal final MemberDetails details) {
        applicantService.updateFavorite(applicantId, details);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/interview-ready")
    @Operation(description = "로그인 유저(면접관)가 면접 준비 완료를 요청한다. 모든 면접관이 준비 완료되었다면 지원자의 면접 단계가 바뀐다.")
    public ResponseEntity<PreparedInterviewersResponse> proceedToInterview(
            @RequestBody final InterviewProceedRequest request,
            @AuthenticationPrincipal final MemberDetails details) {
        return ResponseEntity.ok(applicantService.prepareInterview(request, details));
    }

    @PostMapping("/{applicant-id}/entering-interview")
    @Operation(description = "지원자가 면접 진행 상태로 전환된다.")
    public ResponseEntity<Void> enterInterview(@PathVariable(name = "applicant-id") final Long applicantId) {
        applicantService.enterInterviewProcess(applicantId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{applicant-id}/public")
    @Operation(description = "지원자의 면접 질문 공개를 동의 혹은 비동의한다.")
    public ResponseEntity<Void> makeQuestionsPublic(
            @PathVariable(name = "applicant-id") final Long applicantId,
            @RequestBody final GoQuestionPublicRequest request) {
        applicantService.makeQuestionsPublic(applicantId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/interview-completed")
    @Operation(description = "면접 완료된 지원자들의 목록을 조회한다.")
    public ResponseEntity<List<CompletedApplicantsResponse>> getCompletedApplicants(
            @RequestParam(value = "interview-id") final Long interviewId) {
        return ResponseEntity.status(HttpStatus.OK).body(applicantService.getCompletedApplicants(interviewId));
    }

    @PatchMapping("/interview-completed")
    @Operation(description = "합격자 선정 완료하기 버튼을 눌러서 탈락한 지원자들의 상태를 업데이트한다.")
    public ResponseEntity<String> updateCompletedApplicants(
            @RequestParam(value = "interview-id") final Long interviewId) {
        applicantService.updateCompletedApplicants(interviewId);
        return ResponseEntity.status(HttpStatus.OK).body("합격자 선정이 완료되었습니다.");
    }


    @GetMapping("/interview-completed/details")
    @Operation(description = "특정 지원자의 총점, 순위, 키워드, 한줄평을 조회한다.")
    public ResponseEntity<CompletedApplicantDetailsResponse> getCompletedApplicantDetails(
            @RequestParam(value = "applicant-id") final Long applicantId) {
        return ResponseEntity.status(HttpStatus.OK).body(applicantService.getCompletedApplicantDetails(applicantId));
    }

    @GetMapping("/pass")
    @Operation(description = "면접 진행 완료(COMPLETION) 지원자 중 합격한 지원자 목록을 조회한다.")
    public ResponseEntity<List<PassedApplicantsResponse>> findAllPassedApplicantsByInterview(
            @RequestParam(name = "interview-id") final Long interviewId,
            @AuthenticationPrincipal final MemberDetails details) {
        return ResponseEntity.ok(applicantService.listPassedApplicantsByInterview(interviewId, details));
    }

    @PostMapping("/send-email")
    @Operation(description = "세부 면접 지원자들에게 합불 메일을 전송한다.")
    public ResponseEntity<Void> sendPassEmail(@RequestBody final PassEmailSendRequest request) {
        applicantService.sendPassEmail(request);
        return ResponseEntity.ok().build();
    }
}
