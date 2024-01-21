package com.gotcha.server.project.dto.request;

import com.gotcha.server.project.domain.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ProjectRequest {

    private String name;
    private List<String> emails;

    public Project toEntity() {
        return Project.builder()
                .name(name)
                .build();
    }
}
