package com.gotcha.server.applicant.dto.response;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.InterviewStatus;
import com.gotcha.server.evaluation.dto.response.OneLinerResponse;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.dto.response.InterviewListResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class CompletedApplicantDetailsResponse {
    private String applicantName;
    private Integer totalScore;
    private Integer ranking;
    private List<KeywordResponse> keywords;
    private List<OneLinerResponse> oneLiners;

    @Builder
    public CompletedApplicantDetailsResponse(String applicantName, Integer totalScore, Integer ranking, List<KeywordResponse> keywords, List<OneLinerResponse> oneLiners) {
        this.applicantName = applicantName;
        this.totalScore = totalScore;
        this.ranking = ranking;
        this.keywords = keywords;
        this.oneLiners = oneLiners;
    }

    public static CompletedApplicantDetailsResponse from(Applicant applicant, List<KeywordResponse> keywords, List<OneLinerResponse> oneLiners){
        return CompletedApplicantDetailsResponse.builder()
                .applicantName(applicant.getName())
                .totalScore(applicant.getTotalScore())
                .ranking(applicant.getRanking())
                .keywords(keywords)
                .oneLiners(oneLiners)
                .build();
    }
}
