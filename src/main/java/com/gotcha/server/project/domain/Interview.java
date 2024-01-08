package com.gotcha.server.project.domain;

import com.gotcha.server.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interview extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interview_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    private AreaType area;

    @Enumerated(EnumType.STRING)
    private PositionType position;

    @Builder
    public Interview(final String name, final Project project, final AreaType area, final PositionType position) {
        this.name = name;
        this.project = project;
        this.area = area;
        this.position = position;
    }
}
