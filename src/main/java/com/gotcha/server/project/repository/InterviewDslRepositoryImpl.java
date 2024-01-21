package com.gotcha.server.project.repository;

import com.gotcha.server.member.domain.Member;
import com.gotcha.server.member.domain.QMember;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.domain.QInterview;
import com.gotcha.server.project.domain.QSubcollaborator;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InterviewDslRepositoryImpl implements InterviewDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Interview> getInterviewList(String email, Project project) {
        QSubcollaborator subcollaborator = QSubcollaborator.subcollaborator;
        QInterview interview = QInterview.interview;

        return jpaQueryFactory
                .select(subcollaborator.interview)
                .from(subcollaborator)
                .where(subcollaborator.email.eq(email)
                        .and(interview.project.eq(project)))
                .fetch();
    }

    @Override
    public List<Member> getInterviewerList(Interview interview) {
        QSubcollaborator qsubcollaborator = QSubcollaborator.subcollaborator;
        QMember qmember = QMember.member;

        return jpaQueryFactory
                .select(qmember)
                .from(qmember, qsubcollaborator)
                .where(qsubcollaborator.interview.eq(interview),
                        qsubcollaborator.email.eq(qmember.email))
                .fetch();
    }
}
