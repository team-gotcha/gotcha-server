package com.gotcha.server.project.service;

import com.gotcha.server.project.domain.Collaborator;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.dto.request.ProjectRequest;
import com.gotcha.server.project.repository.CollaboratorRepository;
import com.gotcha.server.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CollaboratorRepository collaboratorRepository;

    public void createProject(ProjectRequest request){
        Project project = request.toEntity();
        projectRepository.save(project);
        createCollaborator(project, request.getEmails());
    }

    public void createCollaborator(Project project, List<String> emails){
        for(String email : emails){
            Collaborator collaborator = Collaborator.builder()
                    .email(email)
                    .project(project)
                    .build();
            collaboratorRepository.save(collaborator);
        }
    }
}
