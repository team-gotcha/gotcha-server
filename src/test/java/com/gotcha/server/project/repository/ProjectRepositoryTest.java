package com.gotcha.server.project.repository;

import com.gotcha.server.common.TestRepository;
import com.gotcha.server.global.config.QueryDslConfig;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Collaborator;
import com.gotcha.server.project.domain.Project;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@Import({TestRepository.class, QueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProjectRepositoryTest {
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("Collaborator 이메일로 프로젝트 조회한다.")
    void Collaborator_이메일로_프로젝트_조회하기() {
        // given
        Member 종미 = 테스트유저("종미");
        Member 윤정 = 테스트유저("윤정");
        Project 프로젝트A = 테스트프로젝트();
        Project 프로젝트B = 테스트프로젝트();
        Collaborator 콜라보레이터1 = 테스트콜라보레이터(종미.getEmail(), 프로젝트A);
        Collaborator 콜라보레이터2 = 테스트콜라보레이터(종미.getEmail(), 프로젝트B);

        testRepository.save(
                종미, 윤정,
                프로젝트A, 프로젝트B,
                콜라보레이터1, 콜라보레이터2);

        // when
        List<Project> 조회결과 = projectRepository.findProjectsByCollaboratorEmail(종미.getEmail());

        // then
        Assertions.assertThat(조회결과)
                .hasSize(2)
                .containsExactlyInAnyOrder(프로젝트A, 프로젝트B);
    }
}