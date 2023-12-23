package com.gotcha.server.evaluation.repository;

import com.gotcha.server.evaluation.domain.OneLiner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OneLinerRepository extends JpaRepository<OneLiner, Long> {
}
