package com.gotcha.server.project.repository;

import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.question.domain.CommonQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface InterviewRepository extends JpaRepository<Interview, Long>, InterviewDslRepository{
}
