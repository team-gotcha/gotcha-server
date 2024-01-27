package com.gotcha.server.applicant.service;

import static com.gotcha.server.applicant.domain.Outcome.FAIL;
import static com.gotcha.server.applicant.domain.Outcome.PASS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Favorite;
import com.gotcha.server.applicant.domain.InterviewStatus;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.applicant.dto.request.GoQuestionPublicRequest;
import com.gotcha.server.applicant.dto.request.InterviewProceedRequest;
import com.gotcha.server.applicant.dto.response.ApplicantResponse;
import com.gotcha.server.applicant.dto.response.ApplicantsResponse;
import com.gotcha.server.applicant.repository.ApplicantRepository;
import com.gotcha.server.applicant.repository.FavoriteRepository;
import com.gotcha.server.auth.dto.request.MemberDetails;
import com.gotcha.server.common.IntegrationTest;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.domain.QuestionPublicType;
import com.gotcha.server.question.repository.IndividualQuestionRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class ApplicantServiceTest extends IntegrationTest {
    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private IndividualQuestionRepository individualQuestionRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Test
    void 지원자_상세정보_조회하기() {
        // given
        Member 종미 = environ.테스트유저_저장하기("종미");
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");
        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");

        environ.테스트면접관_저장하기(지원자A, 종미);
        environ.테스트키워드_저장하기(지원자A, "성실함", KeywordType.TRAIT);
        environ.테스트키워드_저장하기(지원자A, "인턴경험", KeywordType.EXPERIENCE);
        environ.테스트키워드_저장하기(지원자A, "SQL자격증", KeywordType.SKILL);

        // when
        ApplicantResponse 조회결과 = applicantService.findApplicantDetailsById(지원자A.getId());

        // then
        assertEquals("지원자A", 조회결과.getName());
        assertThat(조회결과.getInterviewerNames()).hasSize(1)
                .contains("종미");
        assertThat(조회결과.getTraitKeywords()).hasSize(1)
                .contains("성실함");
        assertThat(조회결과.getExperienceKeywords()).hasSize(1)
                .contains("인턴경험");
        assertThat(조회결과.getSkillKeywords()).hasSize(1)
                .contains("SQL자격증");
    }

    @Test
    @DisplayName("지원자가 면접 진행 중 단계가 되었을 때 공통 질문을 저장한다")
    void 지원자에_공통질문_저장하기() {
        // given
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");

        environ.테스트공통질문_저장하기(테스트면접, "자기소개해주세요.");
        environ.테스트공통질문_저장하기(테스트면접, "장점말해주세요.");

        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");

        Member 종미 = environ.테스트유저_저장하기("종미");
        Interviewer 종미면접관 = environ.테스트면접관_저장하기(지원자A, 종미);

        InterviewProceedRequest 면접_준비완료_요청 = new InterviewProceedRequest(지원자A.getId());
        MemberDetails 로그인유저 = new MemberDetails(종미);

        // when
        applicantService.proceedToInterview(면접_준비완료_요청, 로그인유저);

        // then
        List<IndividualQuestion> 등록된_개별질문들 = individualQuestionRepository.findAllDuringInterview(지원자A);
        assertThat(등록된_개별질문들).hasSize(2);
    }

    @Test
    @DisplayName("지원자의 질문 공개를 동의해도 이전에 비동의하였다면 변경되지 않는다")
    void 질문공개_비동의시_변경불가() {
        // given
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");
        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");

        GoQuestionPublicRequest 비동의요청 = new GoQuestionPublicRequest(false);
        GoQuestionPublicRequest 동의요청 = new GoQuestionPublicRequest(true);

        // when
        applicantService.makeQuestionsPublic(지원자A.getId(), 비동의요청);
        applicantService.makeQuestionsPublic(지원자A.getId(), 동의요청);

        // then
        Applicant 변경된지원자 = applicantRepository.findById(지원자A.getId()).get();
        assertEquals(QuestionPublicType.PENDING, 지원자A.getQuestionPublicType());
        assertEquals(QuestionPublicType.PRIVATE, 변경된지원자.getQuestionPublicType());
    }

    @Test
    void 지원자_즐겨찾기() {
        // given
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");
        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");
        Member 종미 = environ.테스트유저_저장하기("종미");

        MemberDetails 로그인한_종미 = new MemberDetails(종미);

        // when
        applicantService.updateFavorite(지원자A.getId(), 로그인한_종미);

        // then
        Optional<Favorite> 즐겨찾기 = favoriteRepository.findByApplicantAndMember(지원자A, 종미);
        assertNotNull(즐겨찾기);
    }

    @Test
    void 지원자_즐겨찾기_취소하기() {
        // given
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");
        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");
        Member 종미 = environ.테스트유저_저장하기("종미");

        MemberDetails 로그인한_종미 = new MemberDetails(종미);

        // when
        applicantService.updateFavorite(지원자A.getId(), 로그인한_종미);
        applicantService.updateFavorite(지원자A.getId(), 로그인한_종미);

        // then
        Optional<Favorite> 즐겨찾기 = favoriteRepository.findByApplicantAndMember(지원자A, 종미);
        assertTrue(즐겨찾기.isEmpty());
    }

    @Test
    void 지원자목록_조회시_즐겨찾기_여부_반환하기() {
        // given
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");
        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");
        Applicant 지원자B = environ.테스트지원자_저장하기(테스트면접, "지원자B");
        Applicant 지원자C = environ.테스트지원자_저장하기(테스트면접, "지원자C");
        Applicant 지원자D = environ.테스트지원자_저장하기(테스트면접, "지원자D");
        Member 종미 = environ.테스트유저_저장하기("종미");
        environ.테스트즐겨찾기_저장하기(지원자B, 종미);
        environ.테스트즐겨찾기_저장하기(지원자D, 종미);

        // when
        List<ApplicantsResponse> 지원자목록 = applicantService.listApplicantsByInterview(테스트면접.getId(), new MemberDetails(종미));

        // then
        assertThat(지원자목록).hasSize(4)
                .extracting(ApplicantsResponse::getFavorite)
                .contains(false, true, false, true);
    }

    @Test // 로컬 db 연결문제로 테스트 못함
    void 합격자_선정_완료하기() {
        //given
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");
        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");
        Applicant 지원자B = environ.테스트지원자_저장하기(테스트면접, "지원자B");
        지원자A.setInterviewStatus(InterviewStatus.COMPLETION);
        지원자B.setInterviewStatus(InterviewStatus.COMPLETION);
        지원자B.updateOutCome(PASS);

        //when
        applicantService.updateCompletedApplicants(테스트면접.getId());

        //then
        assertEquals(FAIL, 지원자A.getOutcome());
        assertEquals(PASS, 지원자B.getOutcome());
    }
}