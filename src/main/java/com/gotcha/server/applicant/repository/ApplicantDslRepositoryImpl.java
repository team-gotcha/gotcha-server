package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.InterviewStatus;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.applicant.domain.QApplicant;
import com.gotcha.server.applicant.domain.QInterviewer;
import com.gotcha.server.applicant.domain.QKeyword;
import com.gotcha.server.applicant.dto.response.KeywordResponse;
import com.gotcha.server.applicant.dto.response.ApplicantsResponse;
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
    public List<ApplicantsResponse> findAllByInterviewWithKeywords(final Interview interview) {
        QKeyword qKeyword = QKeyword.keyword;

        List<Applicant> applicants = findAllApplicant(interview);
        List<Tuple> keywords = findAllKeywordByApplicants(applicants);

        Map<Applicant, List<KeywordResponse>> keywordMap = applicants.stream()
                .collect(Collectors.toMap(applicant -> applicant, v -> new ArrayList<>()));
        keywords.forEach(tuple -> {
            Applicant applicant = tuple.get(qKeyword.applicant);
            String minName = tuple.get(qKeyword.name.min());
            KeywordType keywordType = tuple.get(qKeyword.keywordType);
            keywordMap.get(applicant).add(new KeywordResponse(minName, keywordType));
        });

        return convertToDto(applicants, keywordMap);
    }

    private List<Applicant> findAllApplicant(final Interview interview) {
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

    private List<Tuple> findAllKeywordByApplicants(final List<Applicant> applicants) {
        QKeyword qKeyword = QKeyword.keyword;

        return jpaQueryFactory
                .select(qKeyword.keywordType, qKeyword.name.min(), qKeyword.applicant)
                .from(qKeyword)
                .where(qKeyword.applicant.in(applicants))
                .groupBy(qKeyword.keywordType, qKeyword.applicant)
                .fetch();
    }

    private List<ApplicantsResponse> convertToDto(final List<Applicant> applicants, final Map<Applicant, List<KeywordResponse>> keywordMap) {
        return applicants.stream()
                .map(a -> ApplicantsResponse.builder()
                        .id(a.getId())
                        .name(a.getName())
                        .status(a.getInterviewStatus())
                        .date(a.getDate())
                        .interviewerProfiles(a.getInterviewers().stream()
                                .map(interviewer -> interviewer.getMember().getProfileUrl())
                                .collect(Collectors.toList()))
                        .questionCount(a.getQuestions().size())
                        .keywords(keywordMap.get(a))
                        .build())
                .collect(Collectors.toList());
    }
}
