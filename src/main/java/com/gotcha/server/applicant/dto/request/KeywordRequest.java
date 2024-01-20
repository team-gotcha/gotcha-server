package com.gotcha.server.applicant.dto.request;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Keyword;
import com.gotcha.server.applicant.domain.KeywordType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class KeywordRequest {

    private Applicant applicant;
    private String name;
    private KeywordType keywordType;

    @Builder
    public KeywordRequest(Applicant applicant, String name, KeywordType keywordType) {
        this.applicant = applicant;
        this.name = name;
        this.keywordType = keywordType;
    }

    public Keyword toEntity() {
        return Keyword.builder()
                .applicant(applicant)
                .name(name)
                .keywordType(keywordType)
                .build();
    }
}
