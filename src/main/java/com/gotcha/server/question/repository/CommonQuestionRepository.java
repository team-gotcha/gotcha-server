package com.gotcha.server.question.repository;

import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.question.domain.CommonQuestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonQuestionRepository extends JpaRepository<CommonQuestion, Long> {
    List<CommonQuestion> findAllByInterview(Interview interview);
}
