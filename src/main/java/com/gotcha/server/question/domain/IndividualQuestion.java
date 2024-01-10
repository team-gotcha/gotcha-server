package com.gotcha.server.question.domain;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.evaluation.domain.Evaluation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "individual_question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndividualQuestion {
    private static final int DEFAULT_IMPORTANCE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer importance;

    @Column(nullable = false)
    private Integer questionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "applicant_id")
    private Applicant applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Interviewer interviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_target_id")
    private IndividualQuestion commentTarget;

    @Column(nullable = false)
    private boolean asking;

    @Column(nullable = false)
    private boolean isCommon;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluation> evaluations = new ArrayList<>();

    @Builder
    public IndividualQuestion(final String content, final Applicant applicant) {
        this.content = content;
        this.importance = DEFAULT_IMPORTANCE;
        this.questionOrder = 0;
        this.applicant = applicant;
        this.asking = false;
        this.isCommon = false;
    }

    public void setApplicant(final Applicant applicant) {
        this.applicant = applicant;
    }

    public void setInterviewer(final Interviewer interviewer) {
        this.interviewer = interviewer;
    }

    public void setQuestionOrder(final Integer questionOrder) {
        this.questionOrder = questionOrder;
    }

    public void setImportance(Integer importance) {
        if(importance == null) {
            importance = DEFAULT_IMPORTANCE;
        }
        this.importance = importance;
    }

    public void askDuringInterview() {
        this.asking = true;
    }

    public void addEvaluation(final Evaluation evaluation) {
        evaluations.add(evaluation);
        evaluation.setQuestion(this);
    }

    public void removeEvaluation(final Evaluation evaluation) {
        evaluations.remove(evaluation);
        evaluation.setQuestion(null);
    }

    public Integer multiplyWeight(final int score) {
        return score * importance;
    }

    public Integer calculateEvaluationScore() {
        return multiplyWeight(evaluations.stream()
                .mapToInt(Evaluation::getScore)
                .sum());
    }
}
