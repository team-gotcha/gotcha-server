package com.gotcha.server.project.controller;

import com.gotcha.server.project.dto.request.ProjectRequest;
import com.gotcha.server.project.dto.response.ProjectResponse;
import com.gotcha.server.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<String> createProject(
            @RequestBody @Valid ProjectRequest request) {
        projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("프로젝트 생성 및 초대 이메일 발송이 완료되었습니다.");
    }

    @GetMapping("{projectId}/emails")
    public ResponseEntity<ProjectResponse> getEmails(@PathVariable Long projectId){
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getEmails(projectId));
    }
}
