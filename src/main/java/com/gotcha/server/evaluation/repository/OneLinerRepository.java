package com.gotcha.server.evaluation.repository;

import com.gotcha.server.evaluation.domain.OneLiner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneLinerRepository extends JpaRepository<OneLiner, Long>, OneLinerDslRepository{
}
