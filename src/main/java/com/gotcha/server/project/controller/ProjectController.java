package com.gotcha.server.project.controller;

import com.gotcha.server.auth.security.MemberDetails;
import com.gotcha.server.project.dto.request.ProjectRequest;
import com.gotcha.server.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<String> createProject(
            @RequestBody @Valid ProjectRequest request,
            @AuthenticationPrincipal MemberDetails member) {
        projectService.createProject(request, member.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body("프로젝트가 생성되었습니다.");
    }
}
