package com.gotcha.server.applicant.repository;

import static com.gotcha.server.common.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.common.RepositoryTest;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class InterviewerRepositoryTest extends RepositoryTest {
    @Autowired
    private InterviewerRepository interviewerRepository;

    @Test
    @DisplayName("면접관이 오늘 예정된 면접 건수를 조회한다.")
    void 오늘_면접_개수_조회하기() {
        //given
        Member 종미 = 테스트유저("종미");
        Project 테스트프로젝트 = 테스트프로젝트("테스트프로젝트");
        Interview 테스트면접 = 테스트면접(테스트프로젝트, "테스트면접");
        testRepository.save(종미, 테스트프로젝트, 테스트면접);

        Applicant 지원자A = 테스트지원자(테스트면접, "지원자A");
        지원자A.setDate(LocalDate.now());
        Applicant 지원자B = 테스트지원자(테스트면접, "지원자B");
        지원자B.setDate(LocalDate.now());
        Applicant 지원자C = 테스트지원자(테스트면접, "지원자C");
        지원자C.setDate(LocalDate.now().plusDays(3L));
        testRepository.save(지원자A, 지원자B, 지원자C);

        testRepository.save(테스트면접관(지원자A, 종미), 테스트면접관(지원자B, 종미), 테스트면접관(지원자C, 종미));

        // when
        long 조회된_면접_개수 = interviewerRepository.countTodayInterview(종미);

        // then
        assertEquals(2L, 조회된_면접_개수);
    }

//    @Test
//    @DisplayName("지원자의 면접관 이름 목록을 조회한다.")
//    void findInterviewerNamesByApplicationId() {
//        // given
//        Member 종미 = 테스트유저("종미");
//        Member 윤정 = 테스트유저("윤정");
//        Project 테스트프로젝트 = 테스트프로젝트();
//        Interview 테스트면접 = 테스트면접(테스트프로젝트, "테스트면접");
//        Applicant 지원자A = 테스트지원자(테스트면접, "지원자A");
//        Interviewer 테스트면접관A = 테스트면접관(지원자A, 종미);
//        Interviewer 테스트면접관B = 테스트면접관(지원자A, 윤정);
//
//        testRepository.save(
//                종미, 윤정, 테스트프로젝트, 테스트면접, 지원자A, 테스트면접관A, 테스트면접관B
//        );
//
//        // when
//        List<String> 조회결과 = interviewerRepository.findInterviewerNamesByApplicationId(지원자A.getId());
//
//        // then
//        assertThat(조회결과).hasSize(2)
//                .containsAll(List.of("윤정", "종미"));
//    }
}
