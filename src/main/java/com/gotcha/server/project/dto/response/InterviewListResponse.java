package com.gotcha.server.project.dto.response;

import com.gotcha.server.project.domain.Interview;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InterviewListResponse {
    private Long interviewId;
    private String interviewName;

    @Builder
    public InterviewListResponse(Long interviewId, String interviewName) {
        this.interviewId = interviewId;
        this.interviewName = interviewName;
    }

    public static InterviewListResponse from(Interview interview){
        return InterviewListResponse.builder()
                .interviewId(interview.getId())
                .interviewName(interview.getName())
                .build();
    }
}
