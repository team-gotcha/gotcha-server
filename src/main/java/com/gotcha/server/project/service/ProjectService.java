package com.gotcha.server.project.service;

import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.mail.service.MailService;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.*;
import com.gotcha.server.project.dto.request.ProjectRequest;
import com.gotcha.server.project.dto.response.InterviewListResponse;
import com.gotcha.server.project.dto.response.ProjectListResponse;
import com.gotcha.server.project.dto.response.ProjectResponse;
import com.gotcha.server.project.dto.response.SidebarResponse;
import com.gotcha.server.project.repository.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final SubcollaboratorRepository subcollaboratorRepository;
    private final MailService mailService;
    private final InterviewDslRepositoryImpl interviewDslRepository;


    public void createProject(ProjectRequest request) {
        validProject(request);

        Project project = request.toEntity();
        projectRepository.save(project);
        createCollaborator(project, request.getEmails());
        sendProjectInvitation(request);
    }

    public void createCollaborator(Project project, List<String> emails) {
        for (String email : emails) {
            Collaborator collaborator = Collaborator.builder()
                    .email(email)
                    .project(project)
                    .build();
            collaboratorRepository.save(collaborator);
        }
    }

    public ProjectResponse getEmails(Long projectId) {
        List<String> emails = collaboratorRepository.findEmailsByProjectId(projectId);
        return ProjectResponse.from(emails);
    }

    public void validProject(ProjectRequest request) {
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

    public void sendProjectInvitation(ProjectRequest request) {
        for (String toEmail : request.getEmails()) {
            String title = "[Gotcha] " + request.getName() + " 프로젝트에 초대되셨습니다.";
            String text = "메인 페이지 링크"; // to-do: 메일 내용 추가하기, 가입 유무에 따라 메일 내용 다르게 보내기
            mailService.sendEmail(toEmail, title, text);
        }
    }

    public SidebarResponse getSidebar(Member member) {
        String email = member.getEmail();
        return SidebarResponse.from(member, getProjectList(email));
    }

    public List<ProjectListResponse> getProjectList(String email) {
        List<Collaborator> collaborators = collaboratorRepository.findAllByEmail(email);
        return collaborators.stream()
                .map(collaborator -> ProjectListResponse.from(collaborator.getProject(), interviewDslRepository.toInterviewListDto(email, collaborator.getProject())))
                .collect(Collectors.toList());
    }
}
