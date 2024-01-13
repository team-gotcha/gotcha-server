package com.gotcha.server.applicant.dto.response;

import com.gotcha.server.applicant.domain.*;
import com.gotcha.server.evaluation.dto.response.OneLinerResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class CompletedApplicantsResponse {
    private String interviewName;
    private String applicantName;
    private LocalDate date;
    private List<String> interviewers;
    private String email;
    private List<KeywordResponse> keywords;
    private Integer totalScore;
    private Integer ranking;
    private InterviewStatus interviewStatus;
    private List<OneLinerResponse> oneLiners;

    @Builder
    public CompletedApplicantsResponse(String interviewName, List<String> interviewers, List<KeywordResponse> keywords, String email, LocalDate date, String name, InterviewStatus interviewStatus, Integer totalScore, Integer ranking, List<OneLinerResponse> oneLiners) {
        this.interviewName = interviewName;
        this.interviewers = interviewers;
        this.keywords = keywords;
        this.email = email;
        this.date = date;
        this.applicantName = name;
        this.interviewStatus = interviewStatus;
        this.totalScore = totalScore;
        this.ranking = ranking;
        this.oneLiners = oneLiners;
    }

    public static List<CompletedApplicantsResponse> generateList(List<Applicant> applicants, Map<Applicant, List<KeywordResponse>> keywordMap, Map<Applicant, List<OneLinerResponse>> oneLinerMap) {
        return applicants.stream()
                .map(applicant -> CompletedApplicantsResponse.builder()
                        .interviewName(applicant.getInterview().getName())
                        .interviewers(getInterviewersNames(applicant))
                        .keywords(keywordMap.get(applicant))
                        .oneLiners(oneLinerMap.get(applicant))
                        .email(applicant.getEmail())
                        .date(applicant.getDate())
                        .name(applicant.getName())
                        .interviewStatus(applicant.getInterviewStatus())
                        .totalScore(applicant.getTotalScore())
                        .ranking(applicant.getRanking())
                        .build())
                .toList();
    }

    private static List<String> getInterviewersNames(Applicant applicant) {
        return applicant.getInterviewers().stream()
                .map(Interviewer::getName)
                .toList();
    }
}
