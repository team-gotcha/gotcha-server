package com.gotcha.server.applicant.dto.response;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.InterviewStatus;
import com.gotcha.server.applicant.domain.Interviewer;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplicantsResponse {
    private Long id;
    private String name;
    private InterviewStatus status;
    private LocalDate date;
    private List<String> interviewerProfiles;
    private Integer questionCount;
    private List<KeywordResponse> keywords;

    @Builder
    public ApplicantsResponse(
            final Long id, final String name, final InterviewStatus status, final LocalDate date,
            final List<String> interviewerProfiles, final Integer questionCount, final List<KeywordResponse> keywords) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.date = date;
        this.interviewerProfiles = interviewerProfiles;
        this.questionCount = questionCount;
        this.keywords = keywords;
    }
}
