package com.gotcha.server.project.service;

import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.mail.service.MailService;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.member.repository.MemberRepository;
import com.gotcha.server.project.domain.*;
import com.gotcha.server.project.dto.request.ProjectRequest;
import com.gotcha.server.project.dto.response.InterviewListResponse;
import com.gotcha.server.project.dto.response.ProjectListResponse;
import com.gotcha.server.project.dto.response.ProjectResponse;
import com.gotcha.server.project.dto.response.SidebarResponse;
import com.gotcha.server.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final SubcollaboratorRepository subcollaboratorRepository;
    private final MailService mailService;
    private final InterviewRepository interviewRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createProject(ProjectRequest request) {
        validProject(request);

        Project project = request.toEntity();
        projectRepository.save(project);
        createCollaborator(project, request.getEmails());
        sendProjectInvitation(request);
    }

    @Transactional
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
            boolean isMember = memberRepository.existsByEmail(toEmail);

            String title = "[Gotcha] GOTCHA에서 " + request.getName() + " 프로젝트에 대한 초대 이메일이 도착했어요!";
            String text;

            if (isMember) {
                Member member = memberRepository.findByEmail(toEmail)
                        .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_FOUND));

                text = "안녕하세요 [" + member.getName() + "]님. GOTCHA에서 " + request.getName() + " 프로젝트에 대한 초대 이메일이 발송되었습니다.\n" +
                        "\n" +
                        "하단의 링크를 클릭하여 " + request.getName() + " 프로젝트 면접에 함께 참여해보세요.\n" +
                        "\n" +
                        "(링크첨부)";
            } else {
                text = "안녕하세요. GOTCHA에서 " + request.getName() + " 프로젝트에 대한 초대 이메일이 발송되었습니다.\n" +
                        "\n" +
                        "하단의 링크를 클릭하여 GOTCHA에 가입한 후 " + request.getName() + " 프로젝트 면접에 함께 참여해보세요.\n" +
                        "\n" +
                        "(링크첨부)";
            }
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
                .map(collaborator -> ProjectListResponse.from(collaborator.getProject(), toInterviewListDto(email, collaborator.getProject())))
                .collect(Collectors.toList());
    }

    public List<InterviewListResponse> toInterviewListDto(String email, Project project) {
        List<Interview> interviews = interviewRepository.getInterviewList(email, project);
        return interviews.stream()
                .map(interview -> InterviewListResponse.from(interview))
                .collect(Collectors.toList());
    }
}
