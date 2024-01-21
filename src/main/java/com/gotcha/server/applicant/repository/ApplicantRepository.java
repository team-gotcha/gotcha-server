package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.InterviewStatus;
import com.gotcha.server.project.domain.Interview;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicantRepository extends JpaRepository<Applicant, Long>, ApplicantDslRepository {
    @Query("SELECT a FROM Applicant a "
            + "JOIN FETCH a.interviewers i "
            + "JOIN FETCH i.member "
            + "WHERE a.id = :applicantId")
    Optional<Applicant> findByIdWithInterviewer(@Param("applicantId") Long id);

    @Query("SELECT a FROM Applicant a "
            + "JOIN FETCH a.interviewers i "
            + "JOIN FETCH i.member "
            + "JOIN FETCH a.interview "
            + "WHERE a.id = :applicantId")
    Optional<Applicant> findByIdWithInterviewAndInterviewers(@Param("applicantId") Long id);

    List<Applicant> findAllByInterview(Interview interview);

    List<Applicant> findByInterviewAndInterviewStatus(Interview interview, InterviewStatus status);
}
