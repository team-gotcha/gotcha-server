package com.gotcha.server.applicant.repository;

import static com.gotcha.server.common.TestFixture.*;
import static com.gotcha.server.common.TestFixture.테스트면접;
import static com.gotcha.server.common.TestFixture.테스트지원자;
import static org.assertj.core.api.Assertions.assertThat;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.applicant.dto.response.ApplicantsResponse;
import com.gotcha.server.common.RepositoryTest;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ApplicantRepositoryTest extends RepositoryTest {
    @Autowired
    private ApplicantRepository applicantRepository;

    @Test
    @DisplayName("면접 별로 지원자를 조회할 때 면접관의 프로필도 조회한다.")
    void 면접별_지원자_조회하기() {
        // given
        Member 종미 = 테스트유저("종미");
        Member 윤정 = 테스트유저("윤정");
        Project 조회할프로젝트 = 테스트프로젝트();
        Interview 조회할면접 = 테스트면접(조회할프로젝트, "테스트면접1");
        Interview 다른면접 = 테스트면접(조회할프로젝트, "테스트면접2");
        Applicant 지원자A = 테스트지원자(조회할면접, "지원자A");
        Applicant 지원자B = 테스트지원자(조회할면접, "지원자B");
        Applicant 지원자C = 테스트지원자(조회할면접, "지원자C");
        Applicant 지원자D = 테스트지원자(다른면접, "지원자D");
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
        List<ApplicantsResponse> 조회결과 = applicantRepository.findAllByInterviewWithKeywords(조회할면접);

        // then
        assertThat(조회결과).hasSize(3)
                .extracting("name")
                .containsAll(List.of("지원자A", "지원자B", "지원자C"));
        assertThat(조회결과)
                .filteredOn(a -> a.getName().equals("지원자A"))
                .flatExtracting(ApplicantsResponse::getInterviewerProfiles)
                .hasSize(2);
    }

    @Test
    @DisplayName("면접 별로 지원자를 조회할 때 태그도 조회한다.")
    void 면접별_지원자_조회하기2() {
        // given
        Member 종미 = 테스트유저("종미");
        Project 조회할프로젝트 = 테스트프로젝트();
        Interview 조회할면접 = 테스트면접(조회할프로젝트, "테스트면접1");
        Applicant 지원자A = 테스트지원자(조회할면접, "지원자A");
        Applicant 지원자B = 테스트지원자(조회할면접, "지원자B");
        Interviewer 종미면접관_지원자A = 테스트면접관(지원자A, 종미);
        Interviewer 종미면접관_지원자B = 테스트면접관(지원자B, 종미);
        지원자A.addInterviewer(종미면접관_지원자A);
        지원자B.addInterviewer(종미면접관_지원자B);

        testRepository.save(
                종미,
                조회할프로젝트, 조회할면접,
                지원자A, 지원자B,
                종미면접관_지원자A, 종미면접관_지원자B,
                테스트키워드(지원자A, "성실함", KeywordType.TRAIT), 테스트키워드(지원자A, "인턴경험", KeywordType.EXPERIENCE), 테스트키워드(지원자A, "SQL자격증", KeywordType.SKILL),
                테스트키워드(지원자B, "성실함", KeywordType.TRAIT), 테스트키워드(지원자B, "깔끔함", KeywordType.TRAIT));

        // when
        List<ApplicantsResponse> 조회결과 = applicantRepository.findAllByInterviewWithKeywords(조회할면접);

        // then
        assertThat(조회결과).hasSize(2)
                .filteredOn(a -> a.getName().equals("지원자A"))
                .flatExtracting(ApplicantsResponse::getKeywords)
                .hasSize(3);
    }
}