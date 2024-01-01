package com.gotcha.server.question.service;

import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.repository.ProjectRepository;
import com.gotcha.server.question.domain.CommonQuestion;
import com.gotcha.server.question.dto.request.CommonQuestionsRequest;
import com.gotcha.server.question.repository.CommonQuestionRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {
    private final CommonQuestionRepository commonQuestionRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void createCommonQuestions(final CommonQuestionsRequest request) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUNT));
        List<String> questionContents = request.questions();

        List<CommonQuestion> questions = questionContents.stream()
                .map(content -> new CommonQuestion(content, project))
                .collect(Collectors.toList());
        commonQuestionRepository.saveAll(questions);
    }
}
