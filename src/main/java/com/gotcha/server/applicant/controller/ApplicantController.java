package com.gotcha.server.applicant.controller;

import com.gotcha.server.applicant.dto.request.ApplicantRequest;
import com.gotcha.server.applicant.dto.request.InterviewProceedRequest;
import com.gotcha.server.applicant.dto.request.PassEmailSendRequest;
import com.gotcha.server.applicant.dto.response.*;
import com.gotcha.server.applicant.service.ApplicantService;
import com.gotcha.server.auth.security.MemberDetails;

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
    private final MemberRepository memberRepository;

    @PostMapping("/interview-ready")
    public ResponseEntity<InterviewProceedResponse> proceedToInterview(
            @RequestBody final InterviewProceedRequest request, @AuthenticationPrincipal final MemberDetails details) {
        return ResponseEntity.ok(applicantService.proceedToInterview(request, details));
    }

    @GetMapping("/todays")
    public ResponseEntity<TodayInterviewResponse> countInterview(@AuthenticationPrincipal final MemberDetails details) {
        return ResponseEntity.ok(applicantService.countTodayInterview(details));
    }

    @GetMapping("")
    public ResponseEntity<List<ApplicantsResponse>> findAllApplicantByInterview(@RequestParam(name = "interview-id") final Long interviewId) {
        return ResponseEntity.ok(applicantService.listApplicantsByInterview(interviewId));
    }

    @GetMapping("/{applicant-id}")
    public ResponseEntity<ApplicantResponse> findApplicantDetailsById(@PathVariable(name = "applicant-id") final Long applicantId) {
        return ResponseEntity.ok(applicantService.findApplicantDetailsById(applicantId));
    }

    @GetMapping("/pass")
    public ResponseEntity<List<PassedApplicantsResponse>> findAllPassedApplicantsByInterview(@RequestParam(name = "interview-id") final Long interviewId) {
        return ResponseEntity.ok(applicantService.listPassedApplicantsByInterview(interviewId));
    }

    @PostMapping("/send-email")
    public ResponseEntity<Void> sendPassEmail(@RequestBody final PassEmailSendRequest request) {
        applicantService.sendPassEmail(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<String> createApplicant(
            @RequestBody @Valid ApplicantRequest request,
            @AuthenticationPrincipal MemberDetails details) {
//        //테스트용 유저 생성
//        Member member = Member.builder()
//                .email("a@gmail.co")
//                .socialId("socialId")
//                .name("이름")
//                .profileUrl("a.jpg")
//                .refreshToken("token")
//                .build();
//        memberRepository.save(member);
//        applicantService.createApplicant(request, member);
        applicantService.createApplicant(request, details.member());
        return ResponseEntity.status(HttpStatus.CREATED).body("면접 지원자 정보가 입력되었습니다.");
    }

    @PostMapping("/files")
    public ResponseEntity<String> addApplicantFiles(
            @RequestParam(required = false) MultipartFile resume,
            @RequestParam(required = false) MultipartFile portfolio,
            @RequestParam(name = "applicant-id") final Long applicantId
    ) throws IOException {
        applicantService.addApplicantFiles(resume, portfolio, applicantId);
        return ResponseEntity.status(HttpStatus.CREATED).body("면접 지원자의 파일이 저장되었습니다.");
    }


    @GetMapping("/interview-completed")
    public ResponseEntity<List<CompletedApplicantsResponse>> getCompletedApplicants(@RequestParam(value = "interview-id") Long interviewId) {
        return ResponseEntity.status(HttpStatus.OK).body(applicantService.getCompletedApplicants(interviewId));
    }

    @GetMapping("/interview-completed/details")
    public ResponseEntity<CompletedApplicantDetailsResponse> getCompletedApplicantDetails(@RequestParam(value = "applicant-id") Long applicantId) {
        return ResponseEntity.status(HttpStatus.OK).body(applicantService.getCompletedApplicantDetails(applicantId));
    }
}
