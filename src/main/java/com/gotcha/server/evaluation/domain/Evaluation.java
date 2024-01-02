package com.gotcha.server.evaluation.domain;

import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.global.domain.BaseTimeEntity;
import com.gotcha.server.question.domain.IndividualQuestion;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    private Integer score;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private IndividualQuestion question;

    private Boolean type; // True(공통), False(개별)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interviewer_id")
    private Interviewer interviewer;
}
