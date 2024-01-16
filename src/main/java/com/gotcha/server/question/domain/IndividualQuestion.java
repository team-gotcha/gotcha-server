package com.gotcha.server.question.domain;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.evaluation.domain.Evaluation;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.member.domain.Member;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndividualQuestion {
    private static final int MIN_IMPORTANCE = 3;
    private static final int MAX_IMPORTANCE = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int importance;

    @Column(nullable = false)
    private int questionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "applicant_id")
    private Applicant applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member member;

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
    public IndividualQuestion(final String content, final Applicant applicant, final Member member, final IndividualQuestion commentTarget) {
        this.content = content;
        this.importance = MIN_IMPORTANCE;
        this.questionOrder = 0;
        this.applicant = applicant;
        this.asking = false;
        this.isCommon = false;
        this.member = member;
        this.commentTarget = commentTarget;
    }

    public void setApplicant(final Applicant applicant) {
        this.applicant = applicant;
    }

    public void setQuestionOrder(final Integer questionOrder) {
        this.questionOrder = questionOrder;
    }

    public void setImportance(int importance) {
        validateImportance(importance);
        this.importance = importance;
    }

    public void validateImportance(int importance) {
        if(importance < MIN_IMPORTANCE || importance > MAX_IMPORTANCE) {
            throw new AppException(ErrorCode.INVALID_IMPORTANCE);
        }
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

    public int multiplyWeight(final int score) {
        return score * importance;
    }

    public double calculateEvaluationScore() {
        int totalSum = evaluations.stream()
                .mapToInt(Evaluation::getScore)
                .sum();
        BigDecimal evaluationCount = new BigDecimal(evaluations.size());
        BigDecimal totalSumWithWeight = new BigDecimal(multiplyWeight(totalSum));
        return totalSumWithWeight.divide(evaluationCount).doubleValue();
    }
}
