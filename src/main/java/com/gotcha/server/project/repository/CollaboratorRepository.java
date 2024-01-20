package com.gotcha.server.project.repository;

import com.gotcha.server.project.domain.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {
    @Query("SELECT c.email FROM Collaborator c WHERE c.project.id = :projectId")
    List<String> findEmailsByProjectId(@Param("projectId") Long projectId);

    List<Collaborator> findAllByEmail(String email);
}
