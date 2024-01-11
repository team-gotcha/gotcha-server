package com.gotcha.server.applicant.dto.response;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.InterviewStatus;
import java.time.LocalDate;
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
public class ApplicantsResponse {
    private Long id;
    private String name;
    private InterviewStatus status;
    private LocalDate date;
    private List<String> interviewerEmails;
    private Integer questionCount;
    private List<KeywordResponse> keywords;

    public static List<ApplicantsResponse> generateList(final List<Applicant> applicants, final Map<Applicant, List<KeywordResponse>> keywordMap) {
        return applicants.stream()
                .map(a -> ApplicantsResponse.builder()
                        .id(a.getId())
                        .name(a.getName())
                        .status(a.getInterviewStatus())
                        .date(a.getDate())
                        .interviewerEmails(a.getInterviewers().stream()
                                .map(interviewer -> interviewer.getMember().getEmail())
                                .toList())
                        .questionCount(a.getQuestions().size())
                        .keywords(keywordMap.get(a))
                        .build())
                .toList();
    }
}
