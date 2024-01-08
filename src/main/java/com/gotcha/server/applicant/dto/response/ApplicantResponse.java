package com.gotcha.server.applicant.dto.response;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.InterviewStatus;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.applicant.domain.Keyword;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.member.domain.Member;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantResponse {
    private Long id;
    private LocalDate date;
    private String name;
    private Integer age;
    private String education;
    private String phoneNumber;
    private String position;
    private String path;
    private InterviewStatus interviewStatus;
    private String resumeLink;
    private String portfolio;
    private List<String> interviewerNames;
    private List<String> traitKeywords;
    private List<String> skillKeywords;
    private List<String> experienceKeywords;

    public static ApplicantResponse from(final Applicant applicant, final List<Keyword> keywords) {
        Map<KeywordType, List<String>> classifiedKeywords = classifyByKeywordType(keywords);
        return ApplicantResponse.builder()
                .id(applicant.getId())
                .date(applicant.getDate())
                .name(applicant.getName())
                .age(applicant.getAge())
                .education(applicant.getEducation())
                .phoneNumber(applicant.getPhoneNumber())
                .position(applicant.getPosition())
                .path(applicant.getPath())
                .interviewStatus(applicant.getInterviewStatus())
                .resumeLink(applicant.getResumeLink())
                .portfolio(applicant.getPortfolio())
                .interviewerNames(getInterviewerNames(applicant.getInterviewers()))
                .traitKeywords(classifiedKeywords.get(KeywordType.TRAIT))
                .skillKeywords(classifiedKeywords.get(KeywordType.SKILL))
                .experienceKeywords(classifiedKeywords.get(KeywordType.EXPERIENCE))
                .build();
    }

    private static List<String> getInterviewerNames(final List<Interviewer> interviewers) {
        return interviewers.stream()
                .map(Interviewer::getMember)
                .map(Member::getName)
                .toList();
    }

    private static Map<KeywordType, List<String>> classifyByKeywordType(final List<Keyword> keywords) {
        return keywords.stream()
                .collect(Collectors.groupingBy(
                        Keyword::getKeywordType,
                        Collectors.mapping(Keyword::getName, Collectors.toList())
                ));
    }
}
