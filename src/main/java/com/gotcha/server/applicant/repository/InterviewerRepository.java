package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.member.domain.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewerRepository extends JpaRepository<Interviewer, Long> {
    @Query("SELECT COUNT(i) "
            + "FROM Interviewer i "
            + "JOIN i.applicant a "
            + "WHERE i.member = :member AND a.date = current_date()")
    long countTodayInterview(@Param("member") Member member);
}
