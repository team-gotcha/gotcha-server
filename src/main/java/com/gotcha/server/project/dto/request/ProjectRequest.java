package com.gotcha.server.project.dto.request;

import com.gotcha.server.project.domain.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ProjectRequest {

    private String name;
    private List<String> emails;

    @Builder
    public ProjectRequest(String name, List<String> emails) {
        this.name = name;
        this.emails = emails;
    }

    public Project toEntity() {
        return Project.builder()
                .name(name)
                .build();
    }
}
