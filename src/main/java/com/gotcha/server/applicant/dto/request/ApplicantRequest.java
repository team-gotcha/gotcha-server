package com.gotcha.server.applicant.dto.request;

import com.gotcha.server.applicant.domain.*;
import com.gotcha.server.project.domain.Interview;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    public Applicant toEntity(Interview interview) {
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

