package com.gotcha.server.project.controller;

import com.gotcha.server.auth.dto.request.MemberDetails;
import com.gotcha.server.project.dto.request.ProjectRequest;
import com.gotcha.server.project.dto.response.ProjectResponse;
import com.gotcha.server.project.dto.response.SidebarResponse;
import com.gotcha.server.project.service.ProjectService;
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
    public ResponseEntity<String> createProject(@RequestBody final ProjectRequest request,
                                                @AuthenticationPrincipal final MemberDetails details) {
        projectService.createProject(request, details.member());
        return ResponseEntity.status(HttpStatus.CREATED).body("프로젝트 생성 및 초대 이메일 발송이 완료되었습니다.");
    }

    @GetMapping("/{projectId}/emails")
    public ResponseEntity<ProjectResponse> getEmails(@PathVariable final Long projectId){
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getEmails(projectId));
    }

    @GetMapping
    public ResponseEntity<SidebarResponse> getSidebar(@AuthenticationPrincipal final MemberDetails details){
//        //테스트용 유저 생성
//        Member member = Member.builder()
//                .email("a@gmail.co")
//                .socialId("socialId")
//                .name("이름")
//                .profileUrl("a.jpg")
//                .refreshToken("token")
//                .build();
//        return ResponseEntity.status(HttpStatus.OK).body(projectService.getSidebar(member));
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getSidebar(details.member()));
    }
}
