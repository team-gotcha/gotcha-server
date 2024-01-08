package com.gotcha.server.question.service;

import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.repository.InterviewRepository;
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
    private final InterviewRepository interviewRepository;

    @Transactional
    public void createCommonQuestions(final CommonQuestionsRequest request) {
        Interview interview = interviewRepository.findById(request.interviewId())
                .orElseThrow(() -> new AppException(ErrorCode.INTERVIEW_NOT_FOUNT));
        List<String> questionContents = request.questions();

        List<CommonQuestion> questions = questionContents.stream()
                .map(content -> new CommonQuestion(content, interview))
                .collect(Collectors.toList());
        commonQuestionRepository.saveAll(questions);
    }
}
