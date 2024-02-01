package com.gotcha.server.applicant.repository;

import static com.gotcha.server.common.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.InterviewStatus;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.applicant.domain.Outcome;
import com.gotcha.server.common.RepositoryTest;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ApplicantRepositoryTest extends RepositoryTest {
    @Autowired
    private ApplicantRepository applicantRepository;

    @Test
    @DisplayName("면접 별로 지원자를 조회할 때 면접관의 이메일도 조회한다.")
    void 면접별_지원자_조회하기() {
        // given
        Member 종미 = 테스트유저("종미");
        Member 윤정 = 테스트유저("윤정");
        Project 조회할프로젝트 = 테스트프로젝트("테스트프로젝트");
        Interview 조회할면접 = 테스트면접(조회할프로젝트, "테스트면접1");
        Interview 다른면접 = 테스트면접(조회할프로젝트, "테스트면접2");
        Applicant 지원자A = 테스트지원자(조회할면접, "지원자A", LocalDate.now());
        Applicant 지원자B = 테스트지원자(조회할면접, "지원자B", LocalDate.now());
        Applicant 지원자C = 테스트지원자(조회할면접, "지원자C", LocalDate.now());
        Applicant 지원자D = 테스트지원자(다른면접, "지원자D", LocalDate.now());
        Interviewer 종미면접관_지원자A = 테스트면접관(지원자A, 종미);
        Interviewer 종미면접관_지원자B = 테스트면접관(지원자B, 종미);
        Interviewer 종미면접관_지원자C = 테스트면접관(지원자C, 종미);
        Interviewer 윤정면접관_지원자A = 테스트면접관(지원자A, 윤정);
        지원자A.addInterviewer(종미면접관_지원자A);
        지원자B.addInterviewer(종미면접관_지원자B);
        지원자C.addInterviewer(종미면접관_지원자C);
        지원자A.addInterviewer(윤정면접관_지원자A);

        testRepository.save(
                종미, 윤정,
                조회할프로젝트, 조회할면접, 다른면접,
                지원자A, 지원자B, 지원자C, 지원자D,
                종미면접관_지원자A, 종미면접관_지원자B, 종미면접관_지원자C, 윤정면접관_지원자A);

        // when
        List<Applicant> 조회결과 = applicantRepository.findAllByInterviewWithInterviewer(조회할면접);

        // then
        assertThat(조회결과).hasSize(3)
                .extracting("name")
                .containsAll(List.of("지원자A", "지원자B", "지원자C"));
        assertThat(조회결과)
                .filteredOn(a -> a.getName().equals("지원자A"))
                .flatExtracting(Applicant::getInterviewers)
                .hasSize(2);
    }

    @Test
    @DisplayName("면접 프로젝트 별 지원자 목록 조회 시 면접일 순으로 정렬하기")
    void 면접일순으로_지원자_정렬하기() {
        // given
        Project 조회할프로젝트 = 테스트프로젝트("테스트프로젝트");
        Interview 조회할면접 = 테스트면접(조회할프로젝트, "테스트면접");
        Applicant 지원자A = 테스트지원자(조회할면접, "지원자A", LocalDate.now().plusDays(1L));
        Applicant 지원자B = 테스트지원자(조회할면접, "지원자B", LocalDate.now().plusDays(5L));
        Applicant 지원자C = 테스트지원자(조회할면접, "지원자C", LocalDate.now().plusDays(2L));
        Applicant 지원자D = 테스트지원자(조회할면접, "지원자D", LocalDate.now());
        testRepository.save(조회할프로젝트, 조회할면접, 지원자A, 지원자B, 지원자C, 지원자D);


        // when
        List<Applicant> 조회결과 = applicantRepository.findAllByInterviewWithInterviewer(조회할면접);

        // then
        assertThat(조회결과).hasSize(4)
                .extracting(Applicant::getName)
                .containsExactly("지원자D", "지원자A", "지원자C", "지원자B");
    }

    @Test
    @DisplayName("합격한 지원자들을 조회한다.")
    void 합격한_지원자목록_조회하기() {
        // given
        Project 조회할프로젝트 = 테스트프로젝트("테스트프로젝트");
        Interview 조회할면접 = 테스트면접(조회할프로젝트, "테스트면접1");
        Applicant 합격지원자A = 평가된_지원자_생성하기(조회할면접, "지원자A", Outcome.PASS);
        Applicant 합격지원자B = 평가된_지원자_생성하기(조회할면접, "지원자B", Outcome.PASS);
        Applicant 미평가지원자 = 테스트지원자(조회할면접, "지원자C", LocalDate.now());
        Applicant 불합격지원자 = 평가된_지원자_생성하기(조회할면접, "지원자D", Outcome.FAIL);
        testRepository.save(조회할프로젝트, 조회할면접, 합격지원자A, 합격지원자B, 미평가지원자, 불합격지원자);

        // when
        List<Applicant> 조회결과 = applicantRepository.findAllPassedApplicants(조회할면접);

        // then
        assertThat(조회결과).hasSize(2);
    }

    private Applicant 평가된_지원자_생성하기(Interview 면접, String 이름, Outcome 결과) {
        Applicant 지원자 = 테스트지원자(면접, 이름, LocalDate.now());
        지원자.setInterviewStatus(InterviewStatus.COMPLETION);
        지원자.updateOutCome(결과);
        return 지원자;
    }

    @Test
    @DisplayName("면접 별로 면접이 완료된 지원자들을 조회한다.")
    void 면접별_면접완료_지원자_조회하기() {
        // given
        Member 종미 = 테스트유저("종미");
        Member 윤정 = 테스트유저("윤정");
        Project 조회할프로젝트 = 테스트프로젝트("테스트프로젝트");
        Interview 조회할면접 = 테스트면접(조회할프로젝트, "테스트면접1");
        Interview 다른면접 = 테스트면접(조회할프로젝트, "테스트면접2");
        Applicant 지원자A = 테스트지원자(조회할면접, "지원자A", LocalDate.now());
        지원자A.setInterviewStatus(InterviewStatus.COMPLETION);
        Applicant 지원자B = 테스트지원자(조회할면접, "지원자B", LocalDate.now());
        지원자B.setInterviewStatus(InterviewStatus.COMPLETION);
        Applicant 지원자C = 테스트지원자(조회할면접, "지원자C", LocalDate.now());
        Applicant 지원자D = 테스트지원자(다른면접, "지원자D", LocalDate.now());

        testRepository.save(
                종미, 윤정,
                조회할프로젝트, 조회할면접, 다른면접,
                지원자A, 지원자B, 지원자C, 지원자D);

        // when
        List<Applicant> 조회결과 = applicantRepository.findByInterviewAndInterviewStatus(조회할면접, InterviewStatus.COMPLETION);

        // then
        assertThat(조회결과).hasSize(2)
                .extracting("name")
                .containsAll(List.of("지원자A", "지원자B"));
    }
}