package com.gotcha.server.evaluation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.auth.security.MemberDetails;
import com.gotcha.server.common.IntegrationTest;
import com.gotcha.server.evaluation.domain.Evaluation;
import com.gotcha.server.evaluation.dto.request.EvaluateRequest;
import com.gotcha.server.evaluation.dto.response.QuestionRankResponse;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.repository.IndividualQuestionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class EvaluationServiceTest extends IntegrationTest {
    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private IndividualQuestionRepository individualQuestionRepository;

    @Test
    void 지원자_평가하기() {
        // given
        Member 종미 = environ.테스트유저_저장하기("종미");
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");
        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");
        environ.테스트면접관_저장하기(지원자A, 종미);

        IndividualQuestion 질문A = environ.테스트개별질문_저장하기(지원자A, "자기소개해주세요.", 1, true);
        IndividualQuestion 질문B = environ.테스트개별질문_저장하기(지원자A, "장점을소개해주세요.", 2, true);

        List<EvaluateRequest> 평가요청 = List.of(
                new EvaluateRequest(질문A.getId(), 4, "좋은인상이있음"), new EvaluateRequest(질문B.getId(), 1, "낫배드"));

        // when & then
        assertDoesNotThrow(() -> {
            evaluationService.evaluate(new MemberDetails(종미), 평가요청);
        });
    }

    @Test
    @DisplayName("질문별 총점을 구하는 로직을 구현하기 전 테스트한다.")
    void 질문별_총점_계산하기() {
        // given
        Member 종미 = environ.테스트유저_저장하기("종미");
        Member 윤정 = environ.테스트유저_저장하기("윤정");
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");
        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");
        Interviewer 종미면접관 = environ.테스트면접관_저장하기(지원자A, 종미);
        Interviewer 윤정면접관 = environ.테스트면접관_저장하기(지원자A, 윤정);

        IndividualQuestion 질문A = environ.테스트개별질문_저장하기(지원자A, "자기소개해주세요.", 1, true);
        IndividualQuestion 질문B = environ.테스트개별질문_저장하기(지원자A, "장점을소개해주세요.", 2, true);

        environ.테스트평가_저장하기(1, "인상이좋다", 질문A, 종미면접관);
        environ.테스트평가_저장하기(2, "굿", 질문A, 윤정면접관);
        environ.테스트평가_저장하기(3, "좋은장점이다", 질문B, 종미면접관);
        environ.테스트평가_저장하기(4, "좋다", 질문B, 윤정면접관);

        // when
        List<IndividualQuestion> 평가된_질문들 = individualQuestionRepository.findAllAfterEvaluation(지원자A);
        Map<Long, Integer> 평가된_질문들_점수합 = 평가된_질문들.stream()
                .collect(Collectors.toMap(
                        IndividualQuestion::getId,
                        question -> question.getEvaluations().stream()
                                .mapToInt(Evaluation::getScore)
                                .sum()
                ));

        List<Long> 질문순위 = new ArrayList<>(평가된_질문들_점수합.keySet());
        질문순위.sort((questionId1, questionId2) ->
                        평가된_질문들_점수합.get(questionId2).compareTo(평가된_질문들_점수합.get(questionId1)));

        // then
        assertThat(평가된_질문들).hasSize(2)
                .extracting(IndividualQuestion::getEvaluations)
                .satisfies(evaluations -> assertThat(evaluations).hasSize(2));
        assertEquals(3, 평가된_질문들_점수합.get(질문A.getId()));
        assertEquals(7, 평가된_질문들_점수합.get(질문B.getId()));
        assertEquals(질문B.getId(), 질문순위.get(0));
    }

    @Test
    @DisplayName("지원자의 각 질문들의 총점과 순위를 구한다.")
    void 질문별_총점_계산하기2() {
        // given
        Member 종미 = environ.테스트유저_저장하기("종미");
        Member 윤정 = environ.테스트유저_저장하기("윤정");
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");
        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");
        Interviewer 종미면접관 = environ.테스트면접관_저장하기(지원자A, 종미);
        Interviewer 윤정면접관 = environ.테스트면접관_저장하기(지원자A, 윤정);

        IndividualQuestion 질문A = environ.테스트개별질문_저장하기(지원자A, "자기소개해주세요.", 1, true);
        IndividualQuestion 질문B = environ.테스트개별질문_저장하기(지원자A, "장점을소개해주세요.", 2, true);

        environ.테스트평가_저장하기(1, "인상이좋다", 질문A, 종미면접관);
        environ.테스트평가_저장하기(2, "굿", 질문A, 윤정면접관);
        environ.테스트평가_저장하기(3, "좋은장점이다", 질문B, 종미면접관);
        environ.테스트평가_저장하기(4, "좋다", 질문B, 윤정면접관);

        // when
        List<QuestionRankResponse> 조회결과 = evaluationService.findQuestionRanks(지원자A.getId());

        // then
        assertThat(조회결과).hasSize(2)
                .extracting(QuestionRankResponse::totalScore)
                .containsExactlyElementsOf(List.of(7, 3));
    }
}