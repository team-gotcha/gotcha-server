package com.gotcha.server.project.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectResponse {
    private List<String> emails;

    @Builder
    public ProjectResponse(List<String> emails) {
        this.emails = emails;
    }

    public static ProjectResponse from(List<String> emails){
        return ProjectResponse.builder()
                .emails(emails)
                .build();
    }
}
