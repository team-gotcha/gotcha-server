package com.gotcha.server.project.repository;

import com.gotcha.server.project.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long>, InterviewDslRepository{
}
