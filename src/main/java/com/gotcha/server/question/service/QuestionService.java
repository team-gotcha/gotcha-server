package com.gotcha.server.question.service;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.question.dto.request.IndividualQuestionRequest;
import com.gotcha.server.applicant.repository.ApplicantRepository;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.repository.InterviewRepository;
import com.gotcha.server.question.domain.CommonQuestion;
import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.dto.request.CommonQuestionsRequest;
import com.gotcha.server.question.dto.response.InterviewQuestionResponse;
import com.gotcha.server.question.repository.CommonQuestionRepository;
import com.gotcha.server.question.repository.IndividualQuestionRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
    private final CommonQuestionRepository commonQuestionRepository;
    private final IndividualQuestionRepository individualQuestionRepository;
    private final InterviewRepository interviewRepository;
    private final ApplicantRepository applicantRepository;

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

    public List<InterviewQuestionResponse> listInterviewQuestions(final Long applicantId) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        List<IndividualQuestion> questions = individualQuestionRepository.findAllDuringInterview(applicant);
        return InterviewQuestionResponse.generateList(questions);
    }

    public void createIndividualQuestion(IndividualQuestionRequest request, Member member){
        validQuestion(request);

        IndividualQuestion question = request.toEntity(member);
        individualQuestionRepository.save(question);
    }

    public void validQuestion(IndividualQuestionRequest request){
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new AppException(ErrorCode.CONTENT_IS_EMPTY);
        }
    }
}
