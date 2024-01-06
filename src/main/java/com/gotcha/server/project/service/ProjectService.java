package com.gotcha.server.project.service;

import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.project.domain.Collaborator;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.dto.request.ProjectRequest;
import com.gotcha.server.project.dto.response.ProjectResponse;
import com.gotcha.server.project.repository.CollaboratorRepository;
import com.gotcha.server.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CollaboratorRepository collaboratorRepository;

    public void createProject(ProjectRequest request){
        validProject(request);

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

    public ProjectResponse getEmails(Long projectId){
        List<String> emails = collaboratorRepository.findEmailsByProjectId(projectId);
        return ProjectResponse.from(emails);
    }

    public void validProject(ProjectRequest request){
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new AppException(ErrorCode.NAME_IS_EMPTY);
        }

        for (String email : request.getEmails()) {
            if (!isValidEmail(email)) {
                throw new AppException(ErrorCode.INVALID_EMAIL);
            }
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        return email.matches(emailRegex);
    }
}
