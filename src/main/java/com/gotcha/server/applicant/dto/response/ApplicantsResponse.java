package com.gotcha.server.applicant.dto.response;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.InterviewStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "면접 진행 단계")
    private InterviewStatus status;

    @Schema(description = "면접 날짜")
    private LocalDate date;

    @Schema(description = "면접관들의 email 목록")
    private List<String> interviewerEmails;

    @Schema(description = "지원자의 모든 질문의 개수")
    private Integer questionCount;

    @Schema(description = "키워드 목록 (타입별하나씩)")
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
