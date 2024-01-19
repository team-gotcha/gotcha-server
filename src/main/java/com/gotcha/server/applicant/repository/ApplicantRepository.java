package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.project.domain.Interview;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicantRepository extends JpaRepository<Applicant, Long>, ApplicantDslRepository {
    @Query("select a from Applicant a "
            + "join fetch a.interviewers i "
            + "join fetch i.member "
            + "where a.id = :applicantId")
    Optional<Applicant> findByIdWithInterviewer(@Param("applicantId") Long id);

    @Query("select a from Applicant a "
            + "join fetch a.interviewers i "
            + "join fetch i.member "
            + "join fetch a.interview "
            + "where a.id = :applicantId")
    Optional<Applicant> findByIdWithInterviewAndInterviewers(@Param("applicantId") Long id);

    List<Applicant> findAllByInterview(Interview interview);
}
