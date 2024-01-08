package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Applicant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicantRepository extends JpaRepository<Applicant, Long>, ApplicantDslRepository {
    @Query("select a from Applicant a "
            + "join fetch a.interviewers i "
            + "join fetch i.member "
            + "where a.id = :applicantId")
    Optional<Applicant> findById(@Param("applicantId") Long id);
}
