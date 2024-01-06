package com.gotcha.server.applicant.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class PreparedInterviewer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "applicant_id")
    private Applicant applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "interviewer_id")
    private Interviewer interviewer;

    public PreparedInterviewer(final Applicant applicant, final Interviewer interviewer) {
        this.applicant = applicant;
        this.interviewer = interviewer;
    }
}
