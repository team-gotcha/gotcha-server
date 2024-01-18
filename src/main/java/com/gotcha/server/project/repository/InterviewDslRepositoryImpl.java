package com.gotcha.server.project.repository;

import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.domain.QInterview;
import com.gotcha.server.project.domain.QSubcollaborator;
import com.gotcha.server.project.dto.response.InterviewListResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InterviewDslRepositoryImpl implements InterviewDslRepository{
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

    public List<InterviewListResponse> toInterviewListDto(String email, Project project) {
        List<Interview> interviews = getInterviewList(email, project);
        return interviews.stream()
                .map(interview -> InterviewListResponse.from(interview))
                .collect(Collectors.toList());
    }
}
