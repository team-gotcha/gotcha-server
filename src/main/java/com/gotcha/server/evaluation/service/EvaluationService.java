package com.gotcha.server.evaluation.service;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.applicant.repository.ApplicantRepository;
import com.gotcha.server.applicant.repository.InterviewerRepository;
import com.gotcha.server.auth.security.MemberDetails;
import com.gotcha.server.evaluation.domain.Evaluation;
import com.gotcha.server.evaluation.domain.OneLiner;
import com.gotcha.server.evaluation.dto.request.EvaluateRequest;
import com.gotcha.server.evaluation.dto.request.OneLinerRequest;
import com.gotcha.server.evaluation.dto.response.EvaluationResponse;
import com.gotcha.server.evaluation.dto.response.QuestionEvaluationResponse;
import com.gotcha.server.evaluation.repository.EvaluationRepository;
import com.gotcha.server.evaluation.repository.OneLinerRepository;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.repository.IndividualQuestionRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EvaluationService {
    private final IndividualQuestionRepository individualQuestionRepository;
    private final EvaluationRepository evaluationRepository;
    private final InterviewerRepository interviewerRepository;
    private final OneLinerRepository oneLinerRepository;
    private final ApplicantRepository applicantRepository;

    @Transactional
    public void evaluate(final MemberDetails details, final List<EvaluateRequest> requests) {
        Interviewer interviewer = interviewerRepository.findByMember(details.member())
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED_INTERVIEWER));

        List<Long> questionIds = requests.stream().map(EvaluateRequest::questionId).toList();
        List<IndividualQuestion> questions = individualQuestionRepository.findAllByIdIn(questionIds);
        if(questions.size() != questionIds.size()) {
            throw new AppException(ErrorCode.QUESTION_NOT_FOUNT);
        }

        Map<Long, IndividualQuestion> questionMap = questions.stream().collect(Collectors.toMap(IndividualQuestion::getId, q->q));
        List<Evaluation> evaluations = requests.stream()
                .map(request -> Evaluation.builder()
                        .score(request.score())
                        .content(request.content())
                        .question(questionMap.get(request.questionId()))
                        .interviewer(interviewer)
                        .build())
                .toList();
        evaluationRepository.saveAll(evaluations);
    }

    @Transactional
    public void createOneLiner(final MemberDetails details, final OneLinerRequest request) {
        Interviewer interviewer = interviewerRepository.findByMember(details.member())
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED_INTERVIEWER));
        Applicant applicant = applicantRepository.findById(request.applicantId())
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        oneLinerRepository.save(new OneLiner(applicant, request.content(), interviewer));
    }

    public QuestionEvaluationResponse findQuestionEvaluations(final Long questionId) {
        IndividualQuestion question = individualQuestionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUNT));
        List<EvaluationResponse> evaluations = evaluationRepository.findAllByQuestion(question).stream()
                .map(e -> new EvaluationResponse(e.getScore(), e.getContent()))
                .toList();
        return new QuestionEvaluationResponse(question.getContent(), question.isCommon(), evaluations);
    }
}
