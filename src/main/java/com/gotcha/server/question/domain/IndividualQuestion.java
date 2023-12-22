package com.gotcha.server.question.domain;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "individual_question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndividualQuestion extends Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Interviewer interviewer;

    @ManyToOne
    @JoinColumn(name = "comment_target_id")
    private IndividualQuestion commentTarget;
}
