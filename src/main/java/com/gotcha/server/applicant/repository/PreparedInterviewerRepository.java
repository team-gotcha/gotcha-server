package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.applicant.domain.PreparedInterviewer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreparedInterviewerRepository extends JpaRepository<PreparedInterviewer, Long> {
    boolean existsByInterviewer(Interviewer interviewer);
    long countByApplicant(Applicant applicant);
}
