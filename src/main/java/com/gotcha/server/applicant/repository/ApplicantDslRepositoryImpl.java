package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.InterviewStatus;
import com.gotcha.server.applicant.domain.Outcome;
import com.gotcha.server.applicant.domain.QApplicant;
import com.gotcha.server.applicant.domain.QInterviewer;
import com.gotcha.server.member.domain.QMember;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.question.domain.QIndividualQuestion;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApplicantDslRepositoryImpl implements ApplicantDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Applicant> findAllByInterviewWithInterviewer(final Interview interview) {
        QApplicant qApplicant = QApplicant.applicant;
        QInterviewer qInterviewer = QInterviewer.interviewer;
        QMember qMember = QMember.member;
        QIndividualQuestion qQuestion = QIndividualQuestion.individualQuestion;

        return jpaQueryFactory
                .select(qApplicant)
                .from(qApplicant)
                .innerJoin(qApplicant.interviewers, qInterviewer)
                .fetchJoin()
                .innerJoin(qInterviewer.member, qMember)
                .fetchJoin()
                .leftJoin(qApplicant.questions, qQuestion)
                .where(qApplicant.interview.eq(interview), qApplicant.interviewStatus.ne(InterviewStatus.ANNOUNCED))
                .orderBy(qApplicant.date.asc())
                .fetch();
    }

    @Override
    public List<Applicant> findAllPassedApplicants(final Interview interview) {
        QApplicant qApplicant = QApplicant.applicant;

        return jpaQueryFactory
                .select(qApplicant)
                .from(qApplicant)
                .where(qApplicant.interview.eq(interview),
                        qApplicant.interviewStatus.eq(InterviewStatus.COMPLETION),
                        qApplicant.outcome.eq(Outcome.PASS))
                .orderBy(qApplicant.totalScore.desc())
                .fetch();
    }
}
