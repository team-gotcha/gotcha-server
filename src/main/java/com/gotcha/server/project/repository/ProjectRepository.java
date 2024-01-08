package com.gotcha.server.project.repository;

import com.gotcha.server.applicant.repository.ApplicantDslRepository;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.dto.response.InterviewListResponse;
import com.gotcha.server.question.domain.CommonQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{
//    @Query("SELECT c.project FROM Collaborator c WHERE c.email = :email")
//    List<Project> findProjectsByCollaboratorEmail(@Param("email") String email);
}
