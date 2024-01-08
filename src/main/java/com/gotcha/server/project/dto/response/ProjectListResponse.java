package com.gotcha.server.project.dto.response;

import com.gotcha.server.project.domain.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectListResponse {
    private Long projectId;
    private String projectName;
    private List<InterviewListResponse> interviews;

    @Builder
    public ProjectListResponse(Long projectId, String projectName, List<InterviewListResponse> interviews) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.interviews = interviews;
    }

    public static ProjectListResponse from(Project project, List<InterviewListResponse> interviews){
        return ProjectListResponse.builder()
                .projectId(project.getId())
                .projectName(project.getName())
                .interviews(interviews)
                .build();
    }
}
