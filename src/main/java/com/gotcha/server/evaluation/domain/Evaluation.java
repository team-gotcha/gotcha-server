package com.gotcha.server.evaluation.domain;

import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.global.domain.BaseTimeEntity;
import com.gotcha.server.question.domain.IndividualQuestion;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Evaluation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private Long id;

    @Column(nullable = false)
    private Integer score;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private IndividualQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interviewer_id", nullable = false)
    private Interviewer interviewer;

    @Builder
    public Evaluation(final Integer score, final String content,
            final IndividualQuestion question, final Interviewer interviewer) {
        this.score = score;
        this.content = content;
        this.question = question;
        this.interviewer = interviewer;
    }

    public void setQuestion(final IndividualQuestion question) {
        this.question = question;
    }
}
