package com.gotcha.server.applicant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
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
    private KeywordType keywordType;

    public Keyword(final Applicant applicant, final String name, final KeywordType keywordType) {
        this.applicant = applicant;
        this.name = name;
        this.keywordType = keywordType;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }
}
