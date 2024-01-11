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
    private Integer score;
    private Integer rank;
    private List<String> keywords;

    public static List<PassedApplicantsResponse> generateList(List<Applicant> applicants, Map<Applicant, List<String>> keywords) {
        return applicants.stream()
                .map(a -> PassedApplicantsResponse.builder()
                        .id(a.getId())
                        .name(a.getName())
                        .score(a.getTotalScore())
                        .rank(a.getRanking())
                        .keywords(keywords.get(a))
                        .build())
                .toList();
    }
}
