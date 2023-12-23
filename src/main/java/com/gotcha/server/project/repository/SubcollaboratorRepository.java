package com.gotcha.server.project.repository;

import com.gotcha.server.project.domain.Subcollaborator;
import com.gotcha.server.question.domain.CommonQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcollaboratorRepository extends JpaRepository<Subcollaborator, Long> {
}
