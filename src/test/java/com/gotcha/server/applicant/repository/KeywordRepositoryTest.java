package com.gotcha.server.applicant.repository;

import static com.gotcha.server.common.TestFixture.테스트면접;
import static com.gotcha.server.common.TestFixture.테스트지원자;
import static com.gotcha.server.common.TestFixture.테스트키워드;
import static com.gotcha.server.common.TestFixture.테스트프로젝트;
import static org.assertj.core.api.Assertions.assertThat;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Keyword;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.applicant.dto.response.KeywordResponse;
import com.gotcha.server.common.RepositoryTest;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class KeywordRepositoryTest extends RepositoryTest {
    @Autowired
    private KeywordRepository keywordRepository;

    @Test
    @DisplayName("지원자들의 키워드를 종류별로 하나씩 조회한다.")
    void 지원자목록의_키워드_조회하기() {
        // given
        Project 조회할프로젝트 = 테스트프로젝트("테스트프로젝트");
        Interview 조회할면접 = 테스트면접(조회할프로젝트, "테스트면접1");
        Applicant 지원자A = 테스트지원자(조회할면접, "지원자A", LocalDate.now());
        Applicant 지원자B = 테스트지원자(조회할면접, "지원자B", LocalDate.now());
        Keyword 지원자A_성실함 = 테스트키워드(지원자A, "성실함", KeywordType.TRAIT);
        Keyword 지원자A_인턴경험 = 테스트키워드(지원자A, "인턴경험", KeywordType.EXPERIENCE);
        Keyword 지원자A_SQL자격증 = 테스트키워드(지원자A, "SQL자격증", KeywordType.SKILL);
        Keyword 지원자B_성실함 = 테스트키워드(지원자B, "성실함", KeywordType.TRAIT);
        Keyword 지원자B_깔끔함 = 테스트키워드(지원자B, "깔끔함", KeywordType.TRAIT);

        testRepository.save(
                조회할프로젝트, 조회할면접,
                지원자A, 지원자B,
                지원자A_성실함, 지원자A_인턴경험, 지원자A_SQL자격증,
                지원자B_성실함, 지원자B_깔끔함);

        // when
        Map<Applicant, List<KeywordResponse>> 조회결과 = keywordRepository.findAllByApplicants(List.of(지원자A, 지원자B));

        // then
        assertThat(조회결과).hasSize(2);
        assertThat(조회결과.get(지원자A)).hasSize(3)
                .extracting(KeywordResponse::name)
                .containsAll(List.of("성실함", "인턴경험", "SQL자격증"));
        assertThat(조회결과.get(지원자B)).hasSize(1)
                .extracting(KeywordResponse::name)
                .contains("깔끔함");
    }
}