package com.gotcha.server.project.controller;

import com.gotcha.server.auth.dto.request.MemberDetails;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.dto.request.ProjectRequest;
import com.gotcha.server.project.dto.response.ProjectResponse;
import com.gotcha.server.project.dto.response.SidebarResponse;
import com.gotcha.server.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(description = "프로젝트를 생성하고 초대 이메일을 발송한다.")
    public ResponseEntity<String> createProject(@RequestBody final ProjectRequest request,
                                                @AuthenticationPrincipal final MemberDetails details) {
        final Project project = projectService.createProject(request, details.member());
        projectService.sendProjectInvitation(request, project.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body("프로젝트 생성 및 초대 이메일 발송이 완료되었습니다.");
    }

    @GetMapping("/{projectId}/emails")
    @Operation(description = "프로젝트에 추가된 유저들의 이메일 목록을 반환한다.")
    public ResponseEntity<ProjectResponse> getEmails(@PathVariable final Long projectId){
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getEmails(projectId));
    }

    @GetMapping
    @Operation(description = "사이드바의 정보들을 반환한다.")
    public ResponseEntity<SidebarResponse> getSidebar(@AuthenticationPrincipal final MemberDetails details){
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getSidebar(details.member()));
    }
}
