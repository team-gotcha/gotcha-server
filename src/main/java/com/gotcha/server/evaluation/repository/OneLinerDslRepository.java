package com.gotcha.server.evaluation.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.evaluation.dto.response.OneLinerResponse;

import java.util.List;
import java.util.Map;

public interface OneLinerDslRepository {
    Map<Applicant, List<OneLinerResponse>> getOneLinersForApplicants(List<Applicant> applicants);
    List<OneLinerResponse> getOneLinersForApplicant(Applicant applicant);
}
