package com.gotcha.server.applicant.repository;

import static com.gotcha.server.common.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gotcha.server.applicant.dto.response.ApplicantsResponse;
import com.gotcha.server.common.TestRepository;
import com.gotcha.server.global.config.QueryDslConfig;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Slf4j
@DataJpaTest
@Import({TestRepository.class, QueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ApplicantRepositoryTest {
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Test
    @DisplayName("면접 별로 지원자를 조회할 때 면접관의 프로필도 조회한다.")
    void 면접별_지원자_조회하기() {
        // given
        testRepository.save(
                종미면접관, 윤정면접관,
                테스트프로젝트, 테스트면접, 테스트면접2,
                지원자A, 지원자B, 지원자C, 지원자D,
                종미면접관_지원자A, 종미면접관_지원자B, 종미면접관_지원자C, 윤정면접관_지원자A);
        지원자A.setName("지원자A");
        지원자B.setName("지원자B");
        지원자C.setName("지원자C");

        // when
        List<ApplicantsResponse> 조회결과 = applicantRepository.findAllByInterviewWithKeywords(테스트면접);

        // then
        assertThat(조회결과).hasSize(3)
                .extracting("name")
                .containsExactly("지원자A", "지원자B", "지원자C");
        assertThat(조회결과)
                .filteredOn(a -> a.getName().equals("지원자A"))
                .flatExtracting(ApplicantsResponse::getInterviewerProfiles)
                .hasSize(2);
    }

    @Test
    @DisplayName("면접 별로 지원자를 조회할 때 태그도 조회한다.")
    void 면접별_지원자_조회하기2() {
        // given
        testRepository.save(
                종미면접관,
                테스트프로젝트, 테스트면접,
                지원자A, 지원자B,
                종미면접관_지원자A, 종미면접관_지원자B,
                지원자A_성실함, 지원자A_인턴경험, 지원자A_SQL자격증,
                지원자B_성실함, 지원자B_깔끔함);
        지원자A.setName("지원자A");
        지원자B.setName("지원자B");

        // when
        List<ApplicantsResponse> 조회결과 = applicantRepository.findAllByInterviewWithKeywords(테스트면접);

        // then
        assertThat(조회결과).hasSize(2)
                .filteredOn(a -> a.getName().equals("지원자A"))
                .flatExtracting(ApplicantsResponse::getKeywords)
                .hasSize(3);
    }
}