package com.gotcha.server.applicant.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gotcha.server.applicant.domain.*;
import com.gotcha.server.applicant.dto.message.OutcomeUpdateMessage;
import com.gotcha.server.applicant.dto.request.*;
import com.gotcha.server.applicant.dto.response.*;
import com.gotcha.server.applicant.event.InterviewStartedEvent;
import com.gotcha.server.applicant.repository.ApplicantRepository;
import com.gotcha.server.applicant.repository.FavoriteRepository;
import com.gotcha.server.applicant.repository.KeywordRepository;
import com.gotcha.server.auth.dto.request.MemberDetails;
import com.gotcha.server.evaluation.domain.QuestionEvaluations;
import com.gotcha.server.evaluation.dto.response.OneLinerResponse;
import com.gotcha.server.evaluation.repository.OneLinerRepository;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.external.service.MailService;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.member.repository.MemberRepository;
import com.gotcha.server.mongo.domain.ApplicantMongo;
import com.gotcha.server.mongo.repository.ApplicantMongoRepository;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.repository.InterviewRepository;

import com.gotcha.server.question.event.QuestionDeterminedEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.repository.IndividualQuestionRepository;
import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.gotcha.server.applicant.domain.Outcome.FAIL;
import static com.gotcha.server.applicant.domain.Outcome.PENDING;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final InterviewRepository interviewRepository;
    private final KeywordRepository keywordRepository;
    private final IndividualQuestionRepository individualQuestionRepository;
    private final OneLinerRepository oneLinerRepository;
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AmazonS3 amazonS3;
    private final MailService mailService;
    private final ApplicantMongoRepository applicantMongoRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public InterviewProceedResponse prepareInterview(final InterviewProceedRequest request, final MemberDetails details) {
        Applicant applicant = applicantRepository.findByIdWithInterviewAndInterviewers(request.applicantId())
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        Interviewer interviewer = applicant.pickInterviewer(details.member());
        interviewer.setPrepared();

        List<Interviewer> interviewers = applicant.getInterviewers();
        long interviewerCount = interviewers.size();
        long preparedInterviewerCount = interviewers.stream().filter(Interviewer::isPrepared).count();
        if (interviewerCount <= preparedInterviewerCount) {
            eventPublisher.publishEvent(new InterviewStartedEvent(applicant));
        }
        return new InterviewProceedResponse(interviewerCount, preparedInterviewerCount);
    }

    @Transactional
    public void enterInterviewProcess(final Long applicantId) {
        Applicant applicant = applicantRepository.findByIdWithInterviewAndInterviewers(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        applicant.setInterviewStatus(InterviewStatus.IN_PROGRESS);
        eventPublisher.publishEvent(new QuestionDeterminedEvent(applicant.getId()));
    }

    public List<ApplicantsResponse> listApplicantsByInterview(final Long interviewId, final MemberDetails details) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new AppException(ErrorCode.INTERVIEW_NOT_FOUNT));
        List<Applicant> applicants = applicantRepository.findAllByInterviewWithInterviewer(interview);

        Map<Applicant, List<KeywordResponse>> applicantsWithKeywords = keywordRepository.findAllByApplicants(applicants);
        Map<Applicant, Boolean> favoritesCheck = checkFavorites(applicants, details.member());

        return ApplicantsResponse.generateList(applicantsWithKeywords, favoritesCheck);
    }

    public Map<Applicant, Boolean> checkFavorites(final List<Applicant> applicants, final Member member) {
        List<Applicant> favorites = favoriteRepository.findAllByMemberAndApplicantIn(member, applicants)
                .stream().map(Favorite::getApplicant).toList();
        return applicants.stream()
                .collect(Collectors.toMap(applicant -> applicant, applicant -> favorites.contains(applicant)));
    }

    public ApplicantResponse findApplicantDetailsById(final Long applicantId) {
        Applicant applicant = applicantRepository.findByIdWithInterviewer(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        List<Keyword> keywords = keywordRepository.findAllByApplicant(applicant);
        return ApplicantResponse.from(applicant, keywords);
    }

    @Transactional
    public void updateFavorite(final Long applicantId, final MemberDetails details) {
        Member member = details.member();
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        Optional<Favorite> favorite = favoriteRepository.findByApplicantAndMember(applicant, member);

        favorite.ifPresentOrElse(
                favoriteRepository::delete,
                () -> favoriteRepository.save(new Favorite(member, applicant)));
    }

    public List<PassedApplicantsResponse> listPassedApplicantsByInterview(final Long interviewId,  final MemberDetails details) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new AppException(ErrorCode.INTERVIEW_NOT_FOUNT));
        List<Applicant> applicants = applicantRepository.findAllPassedApplicants(interview);

        Map<Applicant, List<KeywordResponse>> applicantsWithKeywords = keywordRepository.findAllByApplicants(applicants);
        Map<Applicant, Boolean> favoritesCheck = checkFavorites(applicants, details.member());

        return PassedApplicantsResponse.generateList(applicantsWithKeywords, favoritesCheck);
    }

    @Transactional
    public void makeQuestionsPublic(final Long applicantId, final GoQuestionPublicRequest request) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        applicant.changeQuestionPublicType(request.agree());
    }

    public void sendPassEmail(final PassEmailSendRequest request) {
        Interview interview = interviewRepository.findById(request.interviewId())
                .orElseThrow(() -> new AppException(ErrorCode.INTERVIEW_NOT_FOUNT));
        String interviewName = interview.getName();
        String positionName = interview.getPosition().getValue();
        String projectName = interview.getProject().getName();

        List<Applicant> applicants = applicantRepository.findAllByInterview(interview);
        for (Applicant applicant : applicants) {
            sendEmailAndChangeStatus(applicant, projectName, interviewName, positionName);
        }
    }

    public void sendEmailAndChangeStatus(
            final Applicant applicant, final String projectName, final String interviewName, final String positionName) {
        mailService.sendEmail(
                applicant.getEmail(),
                String.format("%s님의 %s %s %s 면접 지원 결과 내용입니다.", applicant.getName(), projectName, interviewName, positionName),
                applicant.getOutcome().createPassEmailMessage(applicant.getName(), projectName, interviewName, positionName));
        applicant.setInterviewStatus(InterviewStatus.ANNOUNCED);
    }

    @Transactional
    public ApplicantIdResponse createApplicant(ApplicantRequest request, Member member) {
        List<Keyword> keywords = createKeywords(request.getKeywords());
        List<Interviewer> interviewers = createInterviewers(request.getInterviewers());

        Applicant applicant = request.toEntity(findInterviewById(request.getInterviewId()));
        for (Interviewer interviewer : interviewers) {
            applicant.addInterviewer(interviewer);
        }
        for (Keyword keyword : keywords) {
            applicant.addKeyword(keyword);
        }
        applicantRepository.save(applicant);

        ApplicantMongo applicantMongo = ApplicantMongo.builder()
                .applicantId(applicant.getId())
                .outcome(PENDING)
                .build();
        applicantMongoRepository.insert(applicantMongo);

        return new ApplicantIdResponse(applicant.getId());
    }

    public Interview findInterviewById(Long interviewId){
        return interviewRepository.findById(interviewId)
                .orElseThrow(() -> new AppException(ErrorCode.INTERVIEW_NOT_FOUNT));
    }

    @Transactional
    public void addApplicantFiles(MultipartFile resume, MultipartFile portfolio, Long applicantId) throws IOException {
        String resumeLink = saveUploadFile(resume);
        String portfolioLink = saveUploadFile(portfolio);

        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        applicant.updateResumeLink(resumeLink);
        applicant.updatePortfolio(portfolioLink); // save 없어도 jpa에 의해 db update
    }

    @Transactional
    public String saveUploadFile(@Nullable MultipartFile multipartFile) throws IOException {
        if (multipartFile != null) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getSize());

            String originalFilename = multipartFile.getOriginalFilename();
            int index = Objects.requireNonNull(originalFilename).lastIndexOf(".");
            String ext = originalFilename.substring(index + 1);

            String storeFileName = UUID.randomUUID() + "." + ext;
            String key = "upload/" + storeFileName;

            try (InputStream inputStream = multipartFile.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            }

            return amazonS3.getUrl(bucket, key).toString();
        } else {
            return null;
        }
    }

    public List<Keyword> createKeywords(List<KeywordRequest> keywordRequests) {
        return keywordRequests.stream()
                .map(request -> request.toEntity())
                .collect(Collectors.toList());
    }

    public List<Interviewer> createInterviewers(List<InterviewerRequest> interviewerRequests) {
        Set<Long> existingMemberIds = new HashSet<>();
        return interviewerRequests.stream()
                .map(request -> {
                    if (!existingMemberIds.add(request.getId())) {
                        throw new AppException(ErrorCode.DUPLICATE_INTERVIEWER);
                    }
                    return request.toEntity(memberRepository.findById(request.getId())
                            .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_FOUND)));}
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CompletedApplicantsResponse> getCompletedApplicants(Long interviewId) {
        final Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new AppException(ErrorCode.INTERVIEW_NOT_FOUNT));

        final List<Applicant> applicants = resetCompletedApplicants(interview);
        final Map<Applicant, List<KeywordResponse>> keywordMap = keywordRepository.findAllByApplicants(applicants);
        final Map<Applicant, List<OneLinerResponse>> oneLinerMap = oneLinerRepository.getOneLinersForApplicants(applicants);
        return CompletedApplicantsResponse.generateList(applicants, keywordMap, oneLinerMap);
    }

    @Transactional
    public void updateCompletedApplicants(Long interviewId) {
        final Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new AppException(ErrorCode.INTERVIEW_NOT_FOUNT));
        final List<Applicant> applicants = applicantRepository.findByInterviewAndInterviewStatus(interview, InterviewStatus.COMPLETION);

        for(Applicant applicant : applicants){
            if (applicant.getOutcome() == PENDING){
                applicant.updateOutCome(FAIL);
            }
        }
    }

    public List<Applicant> resetCompletedApplicants(Interview interview) {
        final List<Applicant> applicants = applicantRepository.findByInterviewAndInterviewStatus(interview, InterviewStatus.COMPLETION);
        setTotalScore(applicants);
        applicants.sort(Collections.reverseOrder()); // 점수 순으로 정렬
        setRanking(applicants);

        return applicants;
    }

    public void setRanking(List<Applicant> applicants) {
        int currentRank = 1;
        Double currentScore = applicants.get(0).getTotalScore();

        for (int i = 0; i < applicants.size(); i++) {
            Applicant applicant = applicants.get(i);
            if (applicant.getTotalScore() < currentScore) {
                currentRank++;
                currentScore = applicant.getTotalScore();
            }

            applicant.updateRanking(currentRank);
        }
    }

    public void setTotalScore(List<Applicant> applicants) {
        for (Applicant applicant : applicants) {
            List<IndividualQuestion> questions = individualQuestionRepository.findAllAfterEvaluation(applicant);

            if (questions.isEmpty()) {
                applicant.updateTotalScore(0.0);
            } else {
                QuestionEvaluations evaluations = new QuestionEvaluations(questions);
                double perfectScore = evaluations.calculatePerfectScore(questions);
                double totalSumWithWeight = evaluations.calculateTotalQuestionsScore(questions).values().stream()
                        .mapToDouble(Double::doubleValue)
                        .sum();

                double totalScore = totalSumWithWeight / (double) questions.size();
                double finalTotalScore = totalScore * 20 / perfectScore;
                double roundedFinalTotalScore = Math.round(finalTotalScore * 100.0) / 100.0; // 소수점 두자리까지만

                applicant.updateTotalScore(roundedFinalTotalScore);
            }
        }
    }

    public CompletedApplicantDetailsResponse getCompletedApplicantDetails(Long applicantId) {
        final Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));

        final List<Keyword> keywords = keywordRepository.findAllByApplicant(applicant);
        final List<OneLinerResponse> oneLiners = oneLinerRepository.getOneLinersForApplicant(applicant);
        return CompletedApplicantDetailsResponse.from(applicant, keywords, oneLiners);
    }

    @Transactional
    public void updateOutcome(Long applicantId, OutcomeUpdateMessage message) {
        final Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));

        applicant.updateOutCome(message.value());
    }

    @Transactional
    public void updateMongoOutcome(Long applicantId, OutcomeUpdateMessage message) {
        final ApplicantMongo applicant = applicantMongoRepository.findByApplicantId(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));

        applicant.updateOutCome(message.value());
        applicantMongoRepository.save(applicant);
    }
}
