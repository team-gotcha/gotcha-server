package com.gotcha.server.project.dto.request;

import com.gotcha.server.project.domain.AreaType;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.PositionType;
import com.gotcha.server.project.domain.Project;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class InterviewRequest {

    private String name;
    private Project project;
    private List<String> emails;
    @Schema(allowableValues = {"SERVICE","FINANCE","IT","MANUFACTURE","EDUCATION","CONSTRUCTION","MEDICAL","MEDIA","ACADEMIC","DISTRIBUTION","CULTURE","VOLUNTEERING","FASHION"})
    private AreaType area;
    @Schema(allowableValues = {"MARKETING","PLANNING","FINANCE","DESIGN","PRODUCTION","LOGISTICS","MANAGEMENT","RND","MUSIC","DEVELOPMENT","THEATER","VIDEO","FASHION"})
    private PositionType position;

    @Builder
    public InterviewRequest(String name, Project project, AreaType area, PositionType position) {
        this.name = name;
        this.project = project;
        this.area = area;
        this.position = position;
    }

    public Interview toEntity() {
        return Interview.builder()
                .name(name)
                .project(project)
                .area(area)
                .position(position)
                .build();
    }
}
