package com.gotcha.server.project.controller;

import com.gotcha.server.project.dto.request.InterviewRequest;
import com.gotcha.server.project.dto.request.ProjectRequest;
import com.gotcha.server.project.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    public ResponseEntity<String> createInterview(
            @RequestBody @Valid InterviewRequest request) {
        interviewService.createInterview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("세부 면접 생성 및 초대 이메일 발송이 완료되었습니다.");
    }
}
