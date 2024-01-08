package com.gotcha.server.project.dto.response;

import com.gotcha.server.member.domain.Member;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SidebarResponse {
    private String userEmail;
    private String userName;
    private String profileUrl;
    private List<ProjectListResponse> projects;

    @Builder
    public SidebarResponse(String userEmail, String userName, String profileUrl, List<ProjectListResponse> projects) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.profileUrl = profileUrl;
        this.projects = projects;
    }

    public static SidebarResponse from(Member member, List<ProjectListResponse> projects){
        return SidebarResponse.builder()
                .userEmail(member.getEmail())
                .userName(member.getName())
                .profileUrl(member.getProfileUrl())
                .projects(projects)
                .build();
    }
}
