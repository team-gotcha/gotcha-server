package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.InterviewStatus;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.applicant.domain.Outcome;
import com.gotcha.server.applicant.domain.QApplicant;
import com.gotcha.server.applicant.domain.QInterviewer;
import com.gotcha.server.applicant.domain.QKeyword;
import com.gotcha.server.applicant.dto.response.KeywordResponse;
import com.gotcha.server.member.domain.QMember;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.question.domain.QIndividualQuestion;
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
public class ApplicantDslRepositoryImpl implements ApplicantDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Deprecated
    public Map<Applicant, List<KeywordResponse>> findAllByInterviewWithKeywords(List<Applicant> applicants, final Interview interview){
        QKeyword qKeyword = QKeyword.keyword;
        List<Tuple> keywords = findAllKeywordByApplicants(applicants);

        Map<Applicant, List<KeywordResponse>> keywordMap = applicants.stream()
                .collect(Collectors.toMap(applicant -> applicant, v -> new ArrayList<>()));
        keywords.forEach(tuple -> {
            Applicant applicant = tuple.get(qKeyword.applicant);
            String minName = tuple.get(qKeyword.name.min());
            KeywordType keywordType = tuple.get(qKeyword.keywordType);
            keywordMap.get(applicant).add(new KeywordResponse(minName, keywordType));
        });
        return keywordMap;
    }

    /*
    Keyword 조회는 KeywordRepository에서 정의함
     */
    @Override
    @Deprecated
    public List<KeywordResponse> findKeywordsByApplicant(Applicant applicant) {
        QKeyword qKeyword = QKeyword.keyword;
        List<Tuple> keywords = findAllKeywordByApplicants(List.of(applicant));

        return keywords.stream()
                .map(tuple -> {
                    String minName = tuple.get(qKeyword.name.min());
                    KeywordType keywordType = tuple.get(qKeyword.keywordType);
                    return new KeywordResponse(minName, keywordType);
                })
                .collect(Collectors.toList());
    }

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

    /*
    Keyword 조회는 KeywordRepository에서 정의함
     */
    @Deprecated
    private List<Tuple> findAllKeywordByApplicants(final List<Applicant> applicants) {
        QKeyword qKeyword = QKeyword.keyword;

        return jpaQueryFactory
                .select(qKeyword.keywordType, qKeyword.name.min(), qKeyword.applicant)
                .from(qKeyword)
                .where(qKeyword.applicant.in(applicants))
                .groupBy(qKeyword.keywordType, qKeyword.applicant)
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
