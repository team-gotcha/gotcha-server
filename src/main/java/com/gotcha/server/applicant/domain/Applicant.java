package com.gotcha.server.applicant.domain;

import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.question.domain.IndividualQuestion;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interviewer> interviewers = new ArrayList<>();

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IndividualQuestion> questions = new ArrayList<>();

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

    public void addInterviewer(final Interviewer interviewer) {
        interviewers.add(interviewer);
        interviewer.setApplicant(this);
    }

    public void removeInterviewer(final Interviewer interviewer) {
        interviewers.remove(interviewer);
        interviewer.setApplicant(null);
    }

    public void addQuestion(final IndividualQuestion question) {
        questions.add(question);
        question.setApplicant(this);
    }

    public void removeQuestion(final IndividualQuestion question) {
        questions.remove(question);
        question.setApplicant(null);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }
}
