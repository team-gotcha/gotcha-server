package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.member.domain.Member;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewerRepository extends JpaRepository<Interviewer, Long> {
    Optional<Interviewer> findByMember(Member member);
    long countByApplicant(Applicant applicant);
    @Query("select count(i) "
            + "from Interviewer i "
            + "join i.applicant a "
            + "where i.member = :member and a.date = current_date()")
    long countTodayInterview(@Param("member") Member member);

//    @Query("SELECT i.member.name FROM Interviewer i WHERE i.applicant.id = :applicationId")
//    List<String> findInterviewerNamesByApplicationId(@Param("applicationId") Long applicationId);
}
