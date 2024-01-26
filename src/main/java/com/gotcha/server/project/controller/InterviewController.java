package com.gotcha.server.project.controller;

import com.gotcha.server.auth.dto.request.MemberDetails;
import com.gotcha.server.project.dto.request.InterviewRequest;
import com.gotcha.server.project.dto.request.ProjectRequest;
import com.gotcha.server.project.dto.response.InterviewerNamesResponse;
import com.gotcha.server.project.dto.response.ProjectResponse;
import com.gotcha.server.project.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    public ResponseEntity<String> createInterview(@RequestBody final InterviewRequest request,
                                                  @AuthenticationPrincipal final MemberDetails details) {
        interviewService.createInterview(request, details.member());
        return ResponseEntity.status(HttpStatus.CREATED).body("세부 면접 생성 및 초대 이메일 발송이 완료되었습니다.");
    }

    @GetMapping("/{interviewId}/names")
    public ResponseEntity<List<InterviewerNamesResponse>> getNames(@PathVariable final Long interviewId){
        return ResponseEntity.status(HttpStatus.OK).body(interviewService.getInterviewerNames(interviewId));
    }

}
