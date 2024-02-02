package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.project.domain.Interview;
import java.util.List;

public interface ApplicantDslRepository {
    List<Applicant> findAllByInterviewWithInterviewer(Interview interview);
    List<Applicant> findAllPassedApplicants(Interview interview);
}
