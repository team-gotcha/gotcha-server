package com.gotcha.server.project.repository;

import static com.gotcha.server.common.TestFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import com.gotcha.server.common.RepositoryTest;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Collaborator;
import com.gotcha.server.project.domain.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CollaboratorRepositoryTest extends RepositoryTest {
    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Test
    void 유저의_첫번째_프로젝트_조회하기() {
        // given
        Member 종미 = 테스트유저("종미");
        Project 프로젝트A = 테스트프로젝트("프로젝트A");
        Project 프로젝트B = 테스트프로젝트("프로젝트B");
        Collaborator 프로젝트A_콜라보레이터 = 테스트콜라보레이터("종미 email", 프로젝트A);
        Collaborator 프로젝트B_콜라보레이터 = 테스트콜라보레이터("종미 email", 프로젝트B);

        testRepository.save(종미, 프로젝트A, 프로젝트B, 프로젝트A_콜라보레이터, 프로젝트B_콜라보레이터);

        // when
        Collaborator 조회된프로젝트 = collaboratorRepository.findFirstByMember(종미.getEmail()).get();

        // then
        assertEquals(조회된프로젝트.getProject().getId(), 프로젝트A.getId());
    }
}