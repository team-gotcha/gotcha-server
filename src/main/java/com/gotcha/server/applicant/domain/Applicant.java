package com.gotcha.server.applicant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Todo: 면접(Interview Entity) fk 추가하기

    private LocalDate date;
    private String name;
    private Integer age;
    private String education;
    private String phoneNumber;
    private String position;
    private String path;
    private Integer totalScore;
    private Outcome outcome;
    private InterviewStatus interviewStatus;
    private String resumeLink;
    private String portfolio;
}
