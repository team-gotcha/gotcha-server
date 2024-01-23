package com.gotcha.server.question.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.evaluation.domain.QEvaluation;
import com.gotcha.server.member.domain.QMember;
import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.domain.QIndividualQuestion;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionDslRepositoryImpl implements QuestionDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<IndividualQuestion> findAllBeforeInterview(Applicant applicant) {
        QIndividualQuestion qQuestion = QIndividualQuestion.individualQuestion;
        QIndividualQuestion qCommentTarget = new QIndividualQuestion("commentTarget");
        QMember qMember = QMember.member;

        return jpaQueryFactory
                .select(qQuestion)
                .from(qQuestion)
                .where(qQuestion.applicant.eq(applicant))
                .innerJoin(qQuestion.member, qMember)
                .fetchJoin()
                .leftJoin(qQuestion.commentTarget, qCommentTarget)
                .fetchJoin()
                .fetch();
    }

    @Override
    public List<IndividualQuestion> findAllDuringInterview(Applicant applicant) {
        QIndividualQuestion qQuestion = QIndividualQuestion.individualQuestion;

        return jpaQueryFactory
                .select(qQuestion)
                .from(qQuestion)
                .where(qQuestion.applicant.eq(applicant), qQuestion.asking.eq(true))
                .orderBy(qQuestion.questionOrder.asc())
                .fetch();
    }

    @Override
    public List<IndividualQuestion> findAllAfterEvaluation(Applicant applicant) {
        QIndividualQuestion qQuestion = QIndividualQuestion.individualQuestion;
        QEvaluation qEvaluation = QEvaluation.evaluation;

        return jpaQueryFactory
                .select(qQuestion)
                .from(qQuestion)
                .where(qQuestion.applicant.eq(applicant), qQuestion.asking.eq(true))
                .innerJoin(qQuestion.evaluations, qEvaluation)
                .fetchJoin()
                .orderBy(qQuestion.questionOrder.asc())
                .fetch();
    }
}
