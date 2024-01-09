package com.gotcha.server.project.repository;

import com.gotcha.server.project.domain.Collaborator;
import com.gotcha.server.project.domain.Subcollaborator;
import com.gotcha.server.question.domain.CommonQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcollaboratorRepository extends JpaRepository<Subcollaborator, Long> {
    List<Subcollaborator> findAllByEmail(String email);
}
