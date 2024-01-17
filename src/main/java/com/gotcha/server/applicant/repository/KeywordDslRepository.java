package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.dto.response.KeywordResponse;
import java.util.List;
import java.util.Map;

public interface KeywordDslRepository {
    Map<Applicant, List<KeywordResponse>> findAllByApplicants(final List<Applicant> applicants);
}
