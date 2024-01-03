package com.gotcha.server.applicant.domain;

import com.gotcha.server.project.domain.Interview;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "interview_id")
    private Interview interview;

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

    public Applicant(final Interview interview) {
        this.interview = interview;
        this.outcome = Outcome.PENDING;
        this.interviewStatus = InterviewStatus.PREPARATION;
    }

    public void moveToNextStatus() {
        interviewStatus = interviewStatus.moveToNextStatus();
    }
}
