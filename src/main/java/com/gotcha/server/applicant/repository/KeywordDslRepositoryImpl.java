package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.applicant.domain.QKeyword;
import com.gotcha.server.applicant.dto.response.KeywordResponse;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KeywordDslRepositoryImpl implements KeywordDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * Applicants의 keyword들을 type별로 하나씩 조회한다.
     * @param applicants
     * @return applicants with keywords(name, type)
     */
    @Override
    public Map<Applicant, List<KeywordResponse>> findAllByApplicants(final List<Applicant> applicants) {
        QKeyword qKeyword = QKeyword.keyword;
        List<Tuple> tuples = jpaQueryFactory
                .select(qKeyword.keywordType, qKeyword.name.min(), qKeyword.applicant)
                .from(qKeyword)
                .where(qKeyword.applicant.in(applicants))
                .groupBy(qKeyword.keywordType, qKeyword.applicant)
                .fetch();

        Map<Applicant, List<KeywordResponse>> keywordMap = applicants.stream()
                .collect(Collectors.toMap(applicant -> applicant, v -> new ArrayList<>()));
        tuples.forEach(tuple -> {
            Applicant applicant = tuple.get(qKeyword.applicant);
            String minName = tuple.get(qKeyword.name.min());
            KeywordType keywordType = tuple.get(qKeyword.keywordType);
            keywordMap.get(applicant).add(new KeywordResponse(minName, keywordType));
        });
        return keywordMap;
    }
}
