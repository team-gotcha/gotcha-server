package com.gotcha.server.applicant.repository;

import static com.gotcha.server.common.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Favorite;
import com.gotcha.server.common.RepositoryTest;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FavoriteRepositoryTest extends RepositoryTest {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Test
    @DisplayName("지원자 목록에 대해서 유저가 즐겨찾기한 여부를 조회한다.")
    void 지원자목록에서_즐겨찾기여부_조회하기() {
        // given
        Member 종미 = 테스트유저("종미");
        Project 프로젝트 = 테스트프로젝트("테스트프로젝트");
        Interview 면접 = 테스트면접(프로젝트, "테스트면접");
        Applicant 지원자A = 테스트지원자(면접, "지원자A", LocalDate.now());
        Applicant 지원자B = 테스트지원자(면접, "지원자B", LocalDate.now());
        Applicant 지원자C = 테스트지원자(면접, "지원자C", LocalDate.now());
        Applicant 지원자D = 테스트지원자(면접, "지원자D", LocalDate.now());
        Favorite 지원자B_즐겨찾기 = 테스트즐겨찾기(지원자B, 종미);
        Favorite 지원자D_즐겨찾기 = 테스트즐겨찾기(지원자D, 종미);

        testRepository.save(종미, 프로젝트, 면접, 지원자A, 지원자B, 지원자C, 지원자D, 지원자B_즐겨찾기, 지원자D_즐겨찾기);

        // when
        List<Favorite> 조회결과 = favoriteRepository.findAllByMemberAndApplicantIn(종미, List.of(지원자A, 지원자B, 지원자C));

        // then
        assertThat(조회결과).hasSize(1)
                .extracting(Favorite::getApplicant)
                .contains(지원자B);
    }
}