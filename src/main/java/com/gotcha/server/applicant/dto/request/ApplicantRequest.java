package com.gotcha.server.applicant.dto.request;

import com.gotcha.server.applicant.domain.*;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.repository.InterviewRepository;
import com.gotcha.server.question.dto.request.IndividualQuestionRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ApplicantRequest {

    private String name;
    private LocalDate date;
    private List<InterviewerRequest> interviewers;
    private Integer age;
    private String education;
    private String position;
    private String phoneNumber;
    private String path;
    private String email;
    private List<KeywordRequest> keywords;
    private Long interviewId;
    private List<IndividualQuestionRequest> questions;

    @Builder
    public ApplicantRequest(String name, LocalDate date, List<InterviewerRequest> interviewers, Integer age, String education, String position, String phoneNumber, String path, String email, List<KeywordRequest> keywords, Long interviewId, List<IndividualQuestionRequest> questions) {
        this.name = name;
        this.date = date;
        this.interviewers = interviewers;
        this.age = age;
        this.education = education;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.path = path;
        this.email = email;
        this.keywords = keywords;
        this.interviewId = interviewId;
        this.questions = questions;
    }

    public Applicant toEntity(InterviewRepository interviewRepository) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new AppException(ErrorCode.INTERVIEW_NOT_FOUNT));

        return Applicant.builder()
                .name(name)
                .date(date)
                .age(age)
                .education(education)
                .position(position)
                .phoneNumber(phoneNumber)
                .path(path)
                .email(email)
                .interview(interview)
                .build();
    }
}

