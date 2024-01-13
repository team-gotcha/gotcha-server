package com.gotcha.server.evaluation.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.dto.response.KeywordResponse;
import com.gotcha.server.evaluation.dto.response.OneLinerResponse;
import com.gotcha.server.project.domain.Interview;

import java.util.List;
import java.util.Map;

public interface OneLinerDslRepository {
    public Map<Applicant, List<OneLinerResponse>> getOneLinersForApplicants(List<Applicant> applicants);
}
