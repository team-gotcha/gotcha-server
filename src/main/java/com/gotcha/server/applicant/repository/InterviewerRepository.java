package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Interviewer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewerRepository extends JpaRepository<Interviewer, Long> {

}
