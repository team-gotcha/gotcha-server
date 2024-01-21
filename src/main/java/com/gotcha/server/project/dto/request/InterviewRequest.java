package com.gotcha.server.project.dto.request;

import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.project.domain.AreaType;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.PositionType;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class InterviewRequest {

    private String name;
    private Long projectId;
    private List<String> emails;
    @Schema(allowableValues = {"SERVICE","FINANCE","IT","MANUFACTURE","EDUCATION","CONSTRUCTION","MEDICAL","MEDIA","ACADEMIC","DISTRIBUTION","CULTURE","VOLUNTEERING","FASHION"})
    private AreaType area;
    @Schema(allowableValues = {"MARKETING","PLANNING","FINANCE","DESIGN","PRODUCTION","LOGISTICS","MANAGEMENT","RND","MUSIC","DEVELOPMENT","THEATER","VIDEO","FASHION"})
    private PositionType position;

    public Interview toEntity(ProjectRepository projectRepository) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUNT));

        return Interview.builder()
                .name(name)
                .project(project)
                .area(area)
                .position(position)
                .build();
    }
}
