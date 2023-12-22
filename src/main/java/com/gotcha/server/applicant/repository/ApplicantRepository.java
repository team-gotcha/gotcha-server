package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

}
