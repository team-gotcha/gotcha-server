package com.gotcha.server.common;

import static com.gotcha.server.common.TestFixture.*;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Favorite;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.applicant.domain.Keyword;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.applicant.repository.ApplicantRepository;
import com.gotcha.server.applicant.repository.FavoriteRepository;
import com.gotcha.server.applicant.repository.InterviewerRepository;
import com.gotcha.server.applicant.repository.KeywordRepository;
import com.gotcha.server.evaluation.domain.Evaluation;
import com.gotcha.server.evaluation.repository.EvaluationRepository;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.member.repository.MemberRepository;
import com.gotcha.server.mongo.domain.QuestionMongo;
import com.gotcha.server.mongo.repository.QuestionMongoRepository;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.repository.InterviewRepository;
import com.gotcha.server.project.repository.ProjectRepository;
import com.gotcha.server.question.domain.CommonQuestion;
import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.domain.Likes;
import com.gotcha.server.question.repository.CommonQuestionRepository;
import com.gotcha.server.question.repository.IndividualQuestionRepository;
import com.gotcha.server.question.repository.LikeRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IntegrationTestEnviron {
    private final MemberRepository memberRepository;
    private final ApplicantRepository applicantRepository;
    private final ProjectRepository projectRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewerRepository interviewerRepository;
    private final KeywordRepository keywordRepository;
    private final IndividualQuestionRepository individualQuestionRepository;
    private final EvaluationRepository evaluationRepository;
    private final CommonQuestionRepository commonQuestionRepository;
    private final FavoriteRepository favoriteRepository;
    private final LikeRepository likeRepository;
    private final QuestionMongoRepository questionMongoRepository;

    public Member 테스트유저_저장하기(String 이름) {
        return memberRepository.save(테스트유저(이름));
    }

    public Project 테스트프로젝트_저장하기() {
        return projectRepository.save(테스트프로젝트("테스트프로젝트"));
    }

    public Interview 테스트면접_저장하기(Project 프로젝트, String 면접이름) {
        return interviewRepository.save(테스트면접(프로젝트, 면접이름));
    }

    public Applicant 테스트지원자_저장하기(Interview 면접, String 이름) {
        return applicantRepository.save(테스트지원자(면접, 이름, LocalDate.now()));
    }

    public Applicant 테스트지원자_저장하기(Interview 면접, String 이름, LocalDate 면접일) {
        return applicantRepository.save(테스트지원자(면접, 이름, 면접일));
    }

    public void 테스트지원자_질문공개하기(Applicant 지원자) {
        지원자.changeQuestionPublicType(true);
        applicantRepository.save(지원자);
    }

    public Interviewer 테스트면접관_저장하기(Applicant 지원자, Member 면접관) {
        return interviewerRepository.save(테스트면접관(지원자, 면접관));
    }

    public Keyword 테스트키워드_저장하기(Applicant 지원자, String 내용, KeywordType 종류) {
        return keywordRepository.save(테스트키워드(지원자, 내용, 종류));
    }

    public IndividualQuestion 테스트개별질문_저장하기(Applicant 지원자, String 내용, Integer 순서, boolean 면접때질문하기, Integer 중요도, Member 작성자) {
        return individualQuestionRepository.save(테스트개별질문(지원자, 내용, 순서, 면접때질문하기, 중요도, 작성자));
    }

    public CommonQuestion 테스트공통질문_저장하기(Interview 면접, String 내용) {
        return commonQuestionRepository.save(테스트공통질문(면접, 내용));
    }

    public Evaluation 테스트평가_저장하기(Integer 점수, String 평가내용, IndividualQuestion 질문, Member 면접관) {
        return evaluationRepository.save(테스트평가(점수, 평가내용, 질문, 면접관));
    }

    public Favorite 테스트즐겨찾기_저장하기(Applicant 지원자, Member 면접관) {
        return favoriteRepository.save(테스트즐겨찾기(지원자, 면접관));
    }

    public Likes 테스트좋아요_저장하기(IndividualQuestion 질문, Member 면접관) {
        return likeRepository.save(테스트좋아요(질문, 면접관));
    }

    public QuestionMongo 테스트몽고DB질문_저장하기(IndividualQuestion 질문, Long 지원자ID) {
        return questionMongoRepository.save(테스트몽고DB질문(질문, 지원자ID));
    }

    public QuestionMongo 테스트몽고DB질문_수정하기(QuestionMongo 질문, String 수정내용) {
        질문.updateContent(수정내용);
        return questionMongoRepository.save(질문);
    }

    public List<QuestionMongo> 테스트몽고DB질문_조회하기() {
        return questionMongoRepository.findAll();
    }
}
