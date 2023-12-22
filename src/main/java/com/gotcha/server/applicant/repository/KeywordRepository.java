package com.gotcha.server.applicant.repository;

import com.gotcha.server.applicant.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

}
