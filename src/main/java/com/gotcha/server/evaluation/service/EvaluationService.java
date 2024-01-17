package com.gotcha.server.evaluation.service;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.InterviewStatus;
import com.gotcha.server.applicant.repository.ApplicantRepository;
import com.gotcha.server.auth.security.MemberDetails;
import com.gotcha.server.evaluation.domain.Evaluation;
import com.gotcha.server.evaluation.domain.OneLiner;
import com.gotcha.server.evaluation.domain.QuestionEvaluations;
import com.gotcha.server.evaluation.dto.request.EvaluateRequest;
import com.gotcha.server.evaluation.dto.request.OneLinerRequest;
import com.gotcha.server.evaluation.dto.response.EvaluationResponse;
import com.gotcha.server.evaluation.dto.response.QuestionEvaluationResponse;
import com.gotcha.server.evaluation.dto.response.QuestionRankResponse;
import com.gotcha.server.evaluation.repository.EvaluationRepository;
import com.gotcha.server.evaluation.repository.OneLinerRepository;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.repository.IndividualQuestionRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private final OneLinerRepository oneLinerRepository;
    private final ApplicantRepository applicantRepository;

    @Transactional
    public void evaluate(final MemberDetails details, final List<EvaluateRequest> requests) {
        List<IndividualQuestion> questions = getQuestionsBeingEvaluated(requests);
        Applicant applicant = validateApplicantOfQuestions(questions);
        if(applicant.getInterviewStatus() != InterviewStatus.COMPLETION) {
            applicant.moveToNextStatus();
        }

        Map<Long, IndividualQuestion> questionMap = questions.stream().collect(Collectors.toMap(IndividualQuestion::getId, q->q));
        List<Evaluation> evaluations = requests.stream()
                .map(request -> Evaluation.builder()
                        .score(request.score())
                        .content(request.content())
                        .question(questionMap.get(request.questionId()))
                        .member(details.member())
                        .build())
                .toList();
        evaluationRepository.saveAll(evaluations);
    }

    private List<IndividualQuestion> getQuestionsBeingEvaluated(final List<EvaluateRequest> requests) {
        List<Long> questionIds = requests.stream().map(EvaluateRequest::questionId).toList();
        List<IndividualQuestion> questions = individualQuestionRepository.findAllWithApplicantByIdIn(questionIds);
        validateQuestionIds(questionIds, questions);
        return questions;
    }

    private void validateQuestionIds(final List<Long> questionIds, final List<IndividualQuestion> questions) {
        if(questions.size() != questionIds.size()) {
            throw new AppException(ErrorCode.QUESTION_NOT_FOUNT);
        }
    }

    private Applicant validateApplicantOfQuestions(final List<IndividualQuestion> questions) {
        Applicant firstApplicant = questions.get(0).getApplicant();
        boolean allSameApplicant = questions.stream().map(IndividualQuestion::getApplicant)
                .allMatch(applicant -> Objects.equals(firstApplicant, applicant));
        if(!allSameApplicant) {
            throw new AppException(ErrorCode.MULTIPLE_APPLICANT_EVALUATION);
        }
        return firstApplicant;
    }

    @Transactional
    public void createOneLiner(final MemberDetails details, final OneLinerRequest request) {
        Applicant applicant = applicantRepository.findById(request.applicantId())
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        oneLinerRepository.save(new OneLiner(applicant, request.content(), details.member()));
    }

    public QuestionEvaluationResponse findQuestionEvaluations(final Long questionId) {
        IndividualQuestion question = individualQuestionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUNT));
        List<EvaluationResponse> evaluations = evaluationRepository.findAllByQuestion(question).stream()
                .map(e -> new EvaluationResponse(e.getScore(), e.getContent()))
                .toList();
        return new QuestionEvaluationResponse(question.getContent(), question.isCommon(), evaluations);
    }

    public List<QuestionRankResponse> findQuestionRanks(final Long applicantId) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));

        List<IndividualQuestion> questions = individualQuestionRepository.findAllAfterEvaluation(applicant);
        QuestionEvaluations evaluations = new QuestionEvaluations(questions);
        return evaluations.createQuestionRanks();
    }
}
