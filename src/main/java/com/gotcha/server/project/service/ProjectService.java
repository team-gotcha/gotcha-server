package com.gotcha.server.project.service;

import com.gotcha.server.member.domain.Member;
import com.gotcha.server.member.service.MemberService;
import com.gotcha.server.project.domain.Collaborator;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.dto.request.ProjectRequest;
import com.gotcha.server.project.repository.CollaboratorRepository;
import com.gotcha.server.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final MemberService memberService;
    private final ProjectRepository projectRepository;
    private final CollaboratorRepository collaboratorRepository;

    public void createProject(ProjectRequest request, String email){
        Project project = request.toEntity();
        projectRepository.save(project);
        createCollaborator(project, email);
    }

    public void createCollaborator(Project project, String email){
        Member member = memberService.findByEmail(email);
        Collaborator collaborator = Collaborator.builder()
                .member(member)
                .project(project)
                .build();
        collaboratorRepository.save(collaborator);
    }
}
