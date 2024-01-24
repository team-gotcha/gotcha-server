package com.gotcha.server.applicant.dto.response;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Keyword;
import com.gotcha.server.evaluation.dto.response.OneLinerResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CompletedApplicantDetailsResponse {
    private String applicantName;
    private Double totalScore;
    private Integer ranking;
    private List<KeywordResponse> keywords;
    private List<OneLinerResponse> oneLiners;

    @Builder
    public CompletedApplicantDetailsResponse(String applicantName, Double totalScore, Integer ranking, List<KeywordResponse> keywords, List<OneLinerResponse> oneLiners) {
        this.applicantName = applicantName;
        this.totalScore = totalScore;
        this.ranking = ranking;
        this.keywords = keywords;
        this.oneLiners = oneLiners;
    }

    public static CompletedApplicantDetailsResponse from(Applicant applicant, List<Keyword> keywords, List<OneLinerResponse> oneLiners){
        return CompletedApplicantDetailsResponse.builder()
                .applicantName(applicant.getName())
                .totalScore(applicant.getTotalScore())
                .ranking(applicant.getRanking())
                .keywords(keywords.stream()
                        .map(keyword -> new KeywordResponse(keyword.getName(), keyword.getKeywordType()))
                        .toList())
                .oneLiners(oneLiners)
                .build();
    }
}
