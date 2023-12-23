package com.gotcha.server.project.domain;

import com.gotcha.server.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private LayoutType layout;
}
