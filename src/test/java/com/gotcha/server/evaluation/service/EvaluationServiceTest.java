package com.gotcha.server.evaluation.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.auth.security.MemberDetails;
import com.gotcha.server.common.IntegrationTest;
import com.gotcha.server.evaluation.dto.request.EvaluateRequest;
import com.gotcha.server.evaluation.repository.EvaluationRepository;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.question.domain.IndividualQuestion;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EvaluationServiceTest extends IntegrationTest {
    @Autowired
    private EvaluationService evaluationService;

    @Test
    void 지원자_평가하기() {
        // given
        Member 종미 = environ.테스트유저_저장하기("종미");
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");
        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");
        environ.테스트면접관_저장하기(지원자A, 종미);

        IndividualQuestion 질문A = environ.테스트개별질문_저장하기(지원자A, "자기소개해주세요.", 1);
        IndividualQuestion 질문B = environ.테스트개별질문_저장하기(지원자A, "장점을소개해주세요.", 2);

        List<EvaluateRequest> 평가요청 = List.of(
                new EvaluateRequest(질문A.getId(), 4, "좋은인상이있음"), new EvaluateRequest(질문B.getId(), 1, "낫배드"));

        // when & then
        assertDoesNotThrow(() -> {
            evaluationService.evaluate(new MemberDetails(종미), 평가요청);
        });
    }
}