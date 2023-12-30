package com.gotcha.server.project.dto.request;

import com.gotcha.server.project.domain.LayoutType;
import com.gotcha.server.project.domain.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProjectRequest {

    private String teamName;
    private String name;
    private LayoutType layout;

    @Builder
    public ProjectRequest(String teamName, String name, LayoutType layout) {
        this.teamName = teamName;
        this.name = name;
        this.layout = layout;
    }

    public Project toEntity() {
        return Project.builder()
                .teamName(teamName)
                .name(name)
                .layout(layout)
                .build();
    }
}
