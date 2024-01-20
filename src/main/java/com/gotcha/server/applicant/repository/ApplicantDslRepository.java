package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.dto.response.ApplicantsResponse;
import com.gotcha.server.applicant.dto.response.KeywordResponse;
import com.gotcha.server.applicant.dto.response.PassedApplicantsResponse;
import com.gotcha.server.project.domain.Interview;
import java.util.List;
import java.util.Map;

public interface ApplicantDslRepository {
    Map<Applicant, List<KeywordResponse>> findAllByInterviewWithKeywords(List<Applicant> applicants, final Interview interview);
    List<Applicant> findAllByInterviewWithInterviewer(Interview interview);
    List<Applicant> findAllPassedApplicants(Interview interview);
    List<KeywordResponse> findKeywordsByApplicant(Applicant applicant);
}
