package com.gotcha.server.question.service;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.event.InterviewStartedEvent;
import com.gotcha.server.auth.dto.request.MemberDetails;
import com.gotcha.server.evaluation.domain.QuestionEvaluations;
import com.gotcha.server.mongo.domain.QuestionMongo;
import com.gotcha.server.question.domain.Likes;
import com.gotcha.server.question.dto.request.AskingFlagsRequest;
import com.gotcha.server.question.dto.response.IndividualQuestionsResponse;
import com.gotcha.server.question.dto.response.QuestionRankResponse;
import com.gotcha.server.question.domain.QuestionPublicType;
import com.gotcha.server.question.dto.message.QuestionUpdateMessage;
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
import com.gotcha.server.question.dto.response.PreparatoryQuestionResponse;
import com.gotcha.server.question.event.QuestionPreparedEvent;
import com.gotcha.server.question.event.QuestionUpdatedEvent;
import com.gotcha.server.question.repository.CommonQuestionRepository;
import com.gotcha.server.question.repository.IndividualQuestionRepository;
import com.gotcha.server.question.repository.LikeRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {
    private final CommonQuestionRepository commonQuestionRepository;
    private final IndividualQuestionRepository individualQuestionRepository;
    private final InterviewRepository interviewRepository;
    private final ApplicantRepository applicantRepository;
    private final LikeRepository likeRepository;
    private final ApplicationEventPublisher eventPublisher;

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

    public List<IndividualQuestionsResponse> listIndividualQuestions(final Long applicantId, final MemberDetails details) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        List<IndividualQuestion> questions = individualQuestionRepository.findAllBeforeInterview(applicant);

        List<Likes> likes = likeRepository.findAllByQuestionIn(questions);
        Map<IndividualQuestion, Long> likesCount = countLikes(questions, likes);
        Map<IndividualQuestion, Boolean> likesCheck = checkLikes(questions, details.member(), likes);

        return questions.stream()
                .map(question -> IndividualQuestionsResponse
                        .from(question, likesCheck.get(question), likesCount.get(question))).toList();
    }

    private Map<IndividualQuestion, Long> countLikes(final List<IndividualQuestion> questions, final List<Likes> likes) {
        Map<IndividualQuestion, Long> count = likes.stream()
                .collect(Collectors.groupingBy(Likes::getQuestion, Collectors.counting()));
        return questions.stream()
                .collect(Collectors.toMap(
                        question -> question,
                        question -> count.getOrDefault(question, 0L)));
    }

    private Map<IndividualQuestion, Boolean> checkLikes(
            final List<IndividualQuestion> questions, final Member member, final List<Likes> likes) {
        List<IndividualQuestion> likedQuestions = likes.stream()
                .filter(like -> like.isOwnedBy(member))
                .map(Likes::getQuestion).toList();
        return questions.stream()
                .collect(Collectors.toMap(question -> question, question -> likedQuestions.contains(question)));
    }

    @Transactional
    public List<InterviewQuestionResponse> listInterviewQuestions(final Long applicantId) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        List<IndividualQuestion> questions = individualQuestionRepository.findAllDuringInterview(applicant);
        determinePublicType(applicant, questions);
        return InterviewQuestionResponse.generateList(questions);
    }

    private void determinePublicType(final Applicant applicant, final List<IndividualQuestion> questions) {
        if(questions.size() > 0
                && !applicant.getQuestionPublicType().equals(QuestionPublicType.PENDING)
                && questions.get(0).getPublicType().equals(QuestionPublicType.PENDING)) {
            questions.stream().forEach(question -> question.changePublicType(applicant));
        }
    }

    @Transactional
    public void createIndividualQuestion(IndividualQuestionRequest request, Member member){
        validQuestion(request);

        Applicant applicant = applicantRepository.findById(request.getApplicantId())
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        IndividualQuestion individualQuestion = findCommentTarget(request.getCommentTargetId());
        IndividualQuestion question = request.toEntity(member, applicant, individualQuestion);
        individualQuestionRepository.save(question);
    }

    public void validQuestion(IndividualQuestionRequest request){
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new AppException(ErrorCode.CONTENT_IS_EMPTY);
        }
    }

    private IndividualQuestion findCommentTarget(final Long id) {
        if(Objects.isNull(id)) {
            return null;
        }
        return individualQuestionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUNT));
    }

    public List<PreparatoryQuestionResponse> listPreparatoryQuestions(final Long applicantId) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        List<IndividualQuestion> individualQuestions = individualQuestionRepository.findAllDuringInterview(applicant);
        eventPublisher.publishEvent(new QuestionPreparedEvent(individualQuestions, applicantId));
        return individualQuestions.stream().map(PreparatoryQuestionResponse::from).toList();
    }

    @Transactional
    public void updateQuestion(final Long questionId, final QuestionUpdateMessage message) {
        IndividualQuestion question = individualQuestionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUNT));
        QuestionUpdateType updateType = message.type();
        updateType.update(question, message.value());
    }

    public List<QuestionRankResponse> findQuestionRanks(final Long applicantId) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));

        List<IndividualQuestion> questions = individualQuestionRepository.findAllAfterEvaluation(applicant);
        QuestionEvaluations evaluations = new QuestionEvaluations(questions);
        return evaluations.createQuestionRanks();
    }

    @Transactional
    public void changeAskingFlags(AskingFlagsRequest request) {
        IndividualQuestion question = individualQuestionRepository.findById(request.questionId())
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUNT));
        question.changeAsking();
        individualQuestionRepository.save(question);
    }

    @Transactional
    public void like(final Long questionId, final MemberDetails details) {
        Member member = details.member();
        IndividualQuestion question = individualQuestionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUNT));
        Optional<Likes> like = likeRepository.findByQuestionAndMember(question, member);

        like.ifPresentOrElse(
                likeRepository::delete,
                () -> likeRepository.save(new Likes(member, question)));
    }

    @Transactional
    @EventListener(QuestionUpdatedEvent.class)
    public void fetchFinalModifiedQuestions(final QuestionUpdatedEvent event) {
        List<QuestionMongo> mongoQuestions = event.mongoQuestions();
        List<Long> modifiedQuestionIds = mongoQuestions.stream().map(QuestionMongo::getQuestionId).toList();
        List<IndividualQuestion> questions = individualQuestionRepository.findAllByIdIn(modifiedQuestionIds);

        Map<Long, IndividualQuestion> individualQuestionsWithId = questions.stream()
                .collect(Collectors.toMap(question -> question.getId(), question -> question));
        mongoQuestions
                .forEach(question -> question.updateQuestion(individualQuestionsWithId.get(question.getQuestionId())));
    }

    @Transactional
    @EventListener(InterviewStartedEvent.class)
    public void saveCommonQuestionsToApplicant(final InterviewStartedEvent event) {
        Applicant applicant = event.applicant();
        List<CommonQuestion> commonQuestions = commonQuestionRepository.findAllByInterview(applicant.getInterview());
        commonQuestions.stream()
                .map(question -> IndividualQuestion.fromCommonQuestion(question, applicant))
                .forEach(question -> applicant.addQuestion(question));
    }
}
