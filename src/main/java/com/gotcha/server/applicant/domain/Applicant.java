package com.gotcha.server.applicant.domain;

import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.question.domain.IndividualQuestion;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Applicant implements Comparable<Applicant> {
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

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Keyword> keywords = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Outcome outcome;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InterviewStatus interviewStatus;

    @Column(nullable = false)
    private String email;

    private LocalDate date;
    private String name;
    private Integer age;
    private String education;
    private String phoneNumber;
    private String position;
    private String path;
    private Integer totalScore;
    private Integer ranking;
    private String resumeLink;
    private String portfolio;

    public Applicant(final Interview interview) {
        this.interview = interview;
        this.outcome = Outcome.PENDING;
        this.interviewStatus = InterviewStatus.PREPARATION;
        this.ranking = 0;
        this.totalScore = 0;
    }

    public void updateRanking(Integer ranking){
        this.ranking = ranking;
    }

    public void updateResumeLink(String resumeLink){
        this.resumeLink = resumeLink;
    }

    public void updatePortfolio(String portfolio){
        this.portfolio = portfolio;
    }

    public void updateTotalScore(Integer totalScore){
        this.totalScore = totalScore;
    }

    public void moveToNextStatus() {
        interviewStatus = interviewStatus.moveToNextStatus();
    }

    public void determineOutcome(Outcome outcome) {
        this.outcome = outcome;
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

    public void addKeyword(Keyword keyword) {
        keywords.add(keyword);
        keyword.setApplicant(this);
    }

    public void removeKeyword(Keyword keyword) {
        keywords.remove(keyword);
        keyword.setApplicant(null);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    @Override
    public int compareTo(Applicant other) {
        return Integer.compare(this.getTotalScore(), other.getTotalScore());
    }

    @Builder
    public Applicant(Interview interview, List<Interviewer> interviewers, List<IndividualQuestion> questions, List<Keyword> keywords, String email, LocalDate date, String name, Integer age, String education, String phoneNumber, String position, String path, String resumeLink, String portfolio) {
        this.interview = interview;
        this.interviewers = interviewers;
        this.questions = questions;
        this.keywords = keywords;
        this.email = email;
        this.date = date;
        this.name = name;
        this.age = age;
        this.education = education;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.path = path;
        this.resumeLink = resumeLink;
        this.portfolio = portfolio;
        this.outcome = Outcome.PENDING;
        this.interviewStatus = InterviewStatus.PREPARATION;
        this.ranking = 0;
        this.totalScore = 0;
    }
}
