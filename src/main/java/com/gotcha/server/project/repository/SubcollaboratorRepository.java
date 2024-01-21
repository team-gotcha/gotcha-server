package com.gotcha.server.project.repository;

import com.gotcha.server.project.domain.Subcollaborator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcollaboratorRepository extends JpaRepository<Subcollaborator, Long> {
}
