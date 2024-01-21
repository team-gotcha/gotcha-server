package com.gotcha.server.applicant.domain;

import com.gotcha.server.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interviewer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "applicant_id")
    private Applicant applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "member_id")
    private Member member;

    @Column(nullable = false)
    private Boolean prepared;

    @Builder
    public Interviewer(final Applicant applicant, final Member member) {
        this.applicant = applicant;
        this.member = member;
        this.prepared = false;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public void setPrepared() {
        this.prepared = true;
    }

    public Boolean isPrepared() {
        return this.prepared;
    }

    public boolean hasPermission(final Member member) {
        return this.member.getId().equals(member.getId());
    }
}
