package com.gotcha.server.project.repository;

import com.gotcha.server.project.domain.Project;
import com.gotcha.server.question.domain.CommonQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
