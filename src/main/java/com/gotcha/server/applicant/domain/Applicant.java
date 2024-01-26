package com.gotcha.server.applicant.domain;

import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.global.domain.BaseTimeEntity;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.domain.QuestionPublicType;
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
public class Applicant extends BaseTimeEntity implements Comparable<Applicant> {
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionPublicType questionPublicType;

    private LocalDate date;
    private String name;
    private Integer age;
    private String education;
    private String phoneNumber;
    private String position;
    private String path;
    private Double totalScore;
    private Integer ranking;
    private String resumeLink;
    private String portfolio;

    @Builder
    public Applicant(Interview interview, String email, LocalDate date, String name, Integer age, String education, String phoneNumber, String position, String path, String resumeLink, String portfolio) {
        this.interview = interview;
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
        this.totalScore = 0.0;
        this.questionPublicType = QuestionPublicType.PENDING;
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

    public void updateTotalScore(Double totalScore){
        this.totalScore = totalScore;
    }

    public void updateOutCome(Outcome outcome){
        this.outcome = outcome;
    }

    public void moveToNextStatus() {
        interviewStatus = interviewStatus.moveToNextStatus();
    }

    public Interviewer pickInterviewer(final Member member) {
        return interviewers.stream().filter(i -> i.hasPermission(member))
                .findAny().orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED_INTERVIEWER));
    }

    public void changeQuestionPublicType(boolean agree) {
        questionPublicType = questionPublicType.change(agree);
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

    @Override
    public int compareTo(Applicant other) {
        return Double.compare(this.getTotalScore(), other.getTotalScore());
    }
}
