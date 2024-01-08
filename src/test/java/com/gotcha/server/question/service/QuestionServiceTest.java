package com.gotcha.server.question.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.repository.ProjectRepository;
import com.gotcha.server.question.dto.request.CommonQuestionsRequest;
import com.gotcha.server.question.repository.CommonQuestionRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private CommonQuestionRepository commonQuestionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void 공통질문_생성하기() {
        // given
        Long projectId = 1L;
        Project mockProject = new Project("프로젝트이름");
        CommonQuestionsRequest request = new CommonQuestionsRequest(List.of("content1", "content2"), projectId);

        given(projectRepository.findById(1L)).willReturn(Optional.of(mockProject));

        // when
        questionService.createCommonQuestions(request);

        // then
        verify(projectRepository, times(1)).findById(projectId);
        verify(commonQuestionRepository, times(1)).saveAll(any());
    }
}