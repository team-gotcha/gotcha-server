package com.gotcha.server.question.repository;

import com.gotcha.server.question.domain.CommonQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonQuestionRepository extends JpaRepository<CommonQuestion, Long> {

}
