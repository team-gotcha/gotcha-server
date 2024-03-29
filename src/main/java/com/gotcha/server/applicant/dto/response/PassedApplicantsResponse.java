package com.gotcha.server.applicant.dto.response;

import com.gotcha.server.applicant.domain.Applicant;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassedApplicantsResponse {
    private Long id;
    private String name;
    private Double score;
    private Integer rank;
    private List<String> keywords;
    private Boolean favorite;

    public static List<PassedApplicantsResponse> generateList(
            final List<Applicant> orderedApplicants,
            final Map<Applicant, List<KeywordResponse>> applicantsWithKeywords,
            final Map<Applicant, Boolean> favoritesCheck) {
        return orderedApplicants.stream()
                .map(applicant -> PassedApplicantsResponse.builder()
                        .id(applicant.getId())
                        .name(applicant.getName())
                        .score(applicant.getTotalScore())
                        .rank(applicant.getRanking())
                        .keywords(applicantsWithKeywords.get(applicant).stream()
                                .map(KeywordResponse::name).toList())
                        .favorite(favoritesCheck.get(applicant))
                        .build())
                .toList();
    }
}
