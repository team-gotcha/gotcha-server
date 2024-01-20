package com.gotcha.server.applicant.dto.response;

import com.gotcha.server.applicant.domain.KeywordType;

public record KeywordResponse(String name, KeywordType type) {

}
