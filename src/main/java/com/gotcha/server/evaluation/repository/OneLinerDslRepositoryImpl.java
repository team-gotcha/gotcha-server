package com.gotcha.server.evaluation.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.evaluation.domain.QOneLiner;
import com.gotcha.server.evaluation.dto.response.OneLinerResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OneLinerDslRepositoryImpl implements OneLinerDslRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Map<Applicant, List<OneLinerResponse>> getOneLinersForApplicants(List<Applicant> applicants) {
        QOneLiner qOneLiner = QOneLiner.oneLiner;

        Map<Applicant, List<OneLinerResponse>> oneLinerMap = jpaQueryFactory
                .select(qOneLiner.applicant, qOneLiner.content, qOneLiner.member.name)
                .from(qOneLiner)
                .where(qOneLiner.applicant.in(applicants))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(qOneLiner.applicant),
                        Collectors.mapping(
                                tuple -> new OneLinerResponse(
                                        tuple.get(qOneLiner.member.name),
                                        tuple.get(qOneLiner.content)
                                ),
                                Collectors.toList()
                        )
                ));
        return oneLinerMap;
    }

    @Override
    public List<OneLinerResponse> getOneLinersForApplicant(Applicant applicant) {
        QOneLiner qOneLiner = QOneLiner.oneLiner;

        return jpaQueryFactory
                .select(qOneLiner.content, qOneLiner.member.name)
                .from(qOneLiner)
                .where(qOneLiner.applicant.eq(applicant))
                .fetch()
                .stream()
                .map(tuple -> new OneLinerResponse(
                        tuple.get(qOneLiner.member.name),
                        tuple.get(qOneLiner.content)
                ))
                .toList();
    }
}
