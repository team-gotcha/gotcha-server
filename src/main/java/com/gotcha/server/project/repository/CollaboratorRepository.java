package com.gotcha.server.project.repository;

import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Collaborator;
import com.gotcha.server.question.domain.CommonQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {
    @Query("SELECT c.email FROM Collaborator c WHERE c.project.id = :projectId")
    List<String> findEmailsByProjectId(@Param("projectId") Long projectId);

    List<Collaborator> findAllByEmail(String email);
}
