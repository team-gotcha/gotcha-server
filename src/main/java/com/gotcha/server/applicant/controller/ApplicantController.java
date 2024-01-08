package com.gotcha.server.applicant.controller;

import com.gotcha.server.applicant.dto.request.InterviewProceedRequest;
import com.gotcha.server.applicant.dto.response.ApplicantResponse;
import com.gotcha.server.applicant.dto.response.ApplicantsResponse;
import com.gotcha.server.applicant.dto.response.InterviewProceedResponse;
import com.gotcha.server.applicant.dto.response.TodayInterviewResponse;
import com.gotcha.server.applicant.service.ApplicantService;
import com.gotcha.server.auth.security.MemberDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/applicants")
@RequiredArgsConstructor
public class ApplicantController {
    private final ApplicantService applicantService;

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
    public ResponseEntity<List<ApplicantsResponse>> findAllApplicantByInterview(@RequestParam(name = "interview-id") Long interviewId) {
        return ResponseEntity.ok(applicantService.listApplicantByInterview(interviewId));
    }

    @GetMapping("/{applicant-id}")
    public ResponseEntity<ApplicantResponse> findApplicantDetailsById(@PathVariable(name = "applicant-id") Long applicantId) {
        return ResponseEntity.ok(applicantService.findApplicantById(applicantId));
    }
}
