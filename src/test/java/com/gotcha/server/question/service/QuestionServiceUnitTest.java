package com.gotcha.server.question.service;

import static com.gotcha.server.common.TestFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.repository.InterviewRepository;
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
class QuestionServiceUnitTest {
    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private CommonQuestionRepository commonQuestionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void 공통질문_생성하기() {
        // given
        Long interviewId = 1L;
        Project mockProject = 테스트프로젝트("프로젝트A");
        Interview mockInterview = 테스트면접(mockProject, "면접이름");
        CommonQuestionsRequest request = new CommonQuestionsRequest(List.of("content1", "content2"), interviewId);

        given(interviewRepository.findById(1L)).willReturn(Optional.of(mockInterview));

        // when
        questionService.createCommonQuestions(request);

        // then
        verify(interviewRepository, times(1)).findById(interviewId);
        verify(commonQuestionRepository, times(1)).saveAll(any());
    }
}