package com.gotcha.server.applicant.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Applicant applicant;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private KeywordType keywordType;


    @Builder
    public Keyword(final Applicant applicant, final String name, final KeywordType keywordType) {
        this.applicant = applicant;
        this.name = name;
        this.keywordType = keywordType;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }
}
