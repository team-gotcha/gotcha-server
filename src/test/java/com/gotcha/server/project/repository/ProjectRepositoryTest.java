package com.gotcha.server.project.repository;

import com.gotcha.server.common.TestRepository;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Collaborator;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.domain.Subcollaborator;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.List;

import static com.gotcha.server.common.TestFixture.*;

@Slf4j
@DataJpaTest
@Import({TestRepository.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProjectRepositoryTest {
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @Test
    @DisplayName("사용자 이메일로 세부 면접 조회하기")
    void 사용자_이메일로_세부_면접_조회하기() {
        // given
        Member 종미 = 테스트유저("종미");
        Member 윤정 = 테스트유저("윤정");
        Project 프로젝트A = 테스트프로젝트("프로젝트A");
        Project 프로젝트B = 테스트프로젝트("프로젝트B");
        Collaborator 콜라보레이터1 = 테스트콜라보레이터(종미.getEmail(), 프로젝트A);
        Collaborator 콜라보레이터2 = 테스트콜라보레이터(종미.getEmail(), 프로젝트B);
        Interview 인터뷰A = 테스트면접(프로젝트A, "디자이너 면접");
        Interview 인터뷰B = 테스트면접(프로젝트A, "개발자 면접");
        Subcollaborator 서브콜라보레이터1 = 테스트서브콜라보레이터(종미.getEmail(), 인터뷰A);
        Subcollaborator 서브콜라보레이터2 = 테스트서브콜라보레이터(종미.getEmail(), 인터뷰B);

        testRepository.save(
                종미, 윤정,
                프로젝트A, 프로젝트B,
                콜라보레이터1, 콜라보레이터2,
                인터뷰A, 인터뷰B,
                서브콜라보레이터1, 서브콜라보레이터2);

        // when
        List<Interview> 조회결과 = interviewRepository.getInterviewList(종미.getEmail(), 프로젝트A);

        // then
        Assertions.assertThat(조회결과)
                .hasSize(2)
                .containsExactlyInAnyOrder(인터뷰A, 인터뷰B);
    }

    @Test
    @DisplayName("면접에 참여한 멤버 목록 조회하기")
    void 면접에_참여한_멤버_목록_조회하기() {
        // given
        Member 멤버1 = 테스트유저("멤버1");
        Member 멤버2 = 테스트유저("멤버2");
        Member 멤버3 = 테스트유저("멤버3");
        Project 프로젝트 = 테스트프로젝트("테스트프로젝트");
        Interview 면접 = 테스트면접(프로젝트, "면접1");
        Subcollaborator 서브콜라보레이터1 = 테스트서브콜라보레이터(멤버1.getEmail(), 면접);
        Subcollaborator 서브콜라보레이터2 = 테스트서브콜라보레이터(멤버2.getEmail(), 면접);
        Subcollaborator 서브콜라보레이터3 = 테스트서브콜라보레이터("test1@mail.test", 면접);
        Subcollaborator 서브콜라보레이터4 = 테스트서브콜라보레이터("test2@mail.test", 면접);

        testRepository.save(
                멤버1, 멤버2, 멤버3,
                프로젝트,
                면접,
                서브콜라보레이터1, 서브콜라보레이터2, 서브콜라보레이터3, 서브콜라보레이터4);

        // when
        List<Member> 조회결과 = interviewRepository.getInterviewerList(면접);

        // then
        Assertions.assertThat(조회결과)
                .hasSize(2)
                .containsExactly(멤버1, 멤버2);
    }
}