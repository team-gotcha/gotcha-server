package com.gotcha.server.project.service;

import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.mail.service.MailService;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Subcollaborator;
import com.gotcha.server.project.dto.request.InterviewRequest;
import com.gotcha.server.project.repository.InterviewRepository;
import com.gotcha.server.project.repository.SubcollaboratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final SubcollaboratorRepository subcollaboratorRepository;
    private final MailService mailService;

    public void createInterview(InterviewRequest request){
        validInterview(request);

        Interview interview = request.toEntity();
        interviewRepository.save(interview);
        createSubcollaborator(interview, request.getEmails());
        sendInterviewInvitation(request);
    }

    public void createSubcollaborator(Interview interview, List<String> emails){
        for(String email : emails){
            Subcollaborator subcollaborator = Subcollaborator.builder()
                    .email(email)
                    .interview(interview)
                    .build();
            subcollaboratorRepository.save(subcollaborator);
        }
    }

    public void validInterview(InterviewRequest request){
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new AppException(ErrorCode.NAME_IS_EMPTY);
        }
    }

    public void sendInterviewInvitation(InterviewRequest request) {
        for(String toEmail : request.getEmails()){
            String title = "[Gotcha] " + request.getName() + " 세부면접에 초대되셨습니다.";
            String content = "메인 페이지 링크"; // to-do: 메일 내용 추가하기, 가입 유무에 따라 메일 내용 다르게 보내기
            mailService.sendEmail(toEmail, title, content);
        }
    }
}
