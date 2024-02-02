package com.gotcha.server.project.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subcollaborator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subcollaborator_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @Builder
    public Subcollaborator(String email, Interview interview) {
        this.email = email;
        this.interview = interview;
    }
}
