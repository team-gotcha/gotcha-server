package com.gotcha.server.applicant.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.applicant.dto.response.ApplicantResponse;
import com.gotcha.server.common.IntegrationTest;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ApplicantServiceTest extends IntegrationTest {
    @Autowired
    private ApplicantService applicantService;

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
}