package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.dto.response.ApplicantsResponse;
import com.gotcha.server.project.domain.Interview;
import java.util.List;

public interface ApplicantDslRepository {
    List<ApplicantsResponse> findAllByInterviewWithKeywords(Interview interview);
}
