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

    public static List<ApplicantsResponse> generateList(final Map<Applicant, List<KeywordResponse>> applicantsWithKeywords) {
        return applicantsWithKeywords.keySet().stream()
                        .map(applicant -> ApplicantsResponse.builder()
                                .id(applicant.getId())
                                .name(applicant.getName())
                                .status(applicant.getInterviewStatus())
                                .date(applicant.getDate())
                                .interviewerEmails(applicant.getInterviewers().stream()
                                        .map(interviewer -> interviewer.getMember().getEmail())
                                        .toList())
                                .questionCount(applicant.getQuestions().size())
                                .keywords(applicantsWithKeywords.get(applicant))
                                .build())
                                .toList();
    }
}
