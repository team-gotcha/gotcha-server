package com.gotcha.server.mongo.domain;

import com.gotcha.server.applicant.domain.Outcome;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "Applicant")
public class ApplicantMongo {

    @Id
    private String id;

    private Long applicantId;
    private Outcome outcome;

    @Builder
    public ApplicantMongo(Long applicantId, Outcome outcome) {
        this.applicantId = applicantId;
        this.outcome = outcome;
    }

    public void updateOutCome(Outcome outcome){
        this.outcome = outcome;
    }
}
