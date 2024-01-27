package com.gotcha.server.question.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.auth.dto.request.MemberDetails;
import com.gotcha.server.common.IntegrationTest;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.domain.QuestionPublicType;
import com.gotcha.server.question.dto.message.QuestionUpdateMessage;
import com.gotcha.server.question.dto.response.IndividualQuestionsResponse;
import com.gotcha.server.question.dto.response.QuestionRankResponse;
import com.gotcha.server.question.event.QuestionPreparedEvent;
import com.gotcha.server.question.repository.IndividualQuestionRepository;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@Slf4j
@RecordApplicationEvents
public class QuestionServiceIntegrationTest extends IntegrationTest {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private IndividualQuestionRepository questionRepository;

    @Autowired
    ApplicationEvents events;

    @Test
    @DisplayName("면접 전 질문 목록을 조회한다. 이때 작성자에 대한 정보를 포함한다.")
    void 면접전_질문목록_조회하기() {
        // given
        Member 종미 = environ.테스트유저_저장하기("종미");
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");
        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");
        IndividualQuestion 질문A = environ.테스트개별질문_저장하기(지원자A, "자기소개해주세요.", 1, true, 5, 종미);
        IndividualQuestion 질문B = environ.테스트개별질문_저장하기(지원자A, "장점을소개해주세요.", 2, true, 5, 종미);

        // when
        List<IndividualQuestionsResponse> 조회결과 = questionService.listIndividualQuestions(지원자A.getId(), new MemberDetails(종미));

        // then
        assertThat(조회결과).hasSize(2)
                .extracting(IndividualQuestionsResponse::getWriterName)
                .contains("종미");
    }

    @Test
    @DisplayName("면접 전 질문 목록 조회 시 좋아요 개수와 로그인 유저의 좋아요 여부를 함께 반환한다.")
    void 질문목록_좋아요_개수_조회하기() {
        // given
        Member 종미 = environ.테스트유저_저장하기("종미");
        Member 윤정 = environ.테스트유저_저장하기("윤정");
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");
        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");
        IndividualQuestion 질문A = environ.테스트개별질문_저장하기(지원자A, "자기소개해주세요.", 1, true, 5, 종미);
        IndividualQuestion 질문B = environ.테스트개별질문_저장하기(지원자A, "장점을소개해주세요.", 2, true, 5, 종미);
        IndividualQuestion 질문C = environ.테스트개별질문_저장하기(지원자A, "협업했을때어려웠던점은?.", 3, true, 5, 종미);

        environ.테스트좋아요_저장하기(질문A, 종미);
        environ.테스트좋아요_저장하기(질문A, 윤정);
        environ.테스트좋아요_저장하기(질문B, 종미);

        // when
        List<IndividualQuestionsResponse> 조회결과 = questionService.listIndividualQuestions(지원자A.getId(), new MemberDetails(종미));

        // then
        assertThat(조회결과).hasSize(3)
                .extracting(IndividualQuestionsResponse::getLikeCount)
                .containsAll(List.of(2L, 1L, 0L));
        assertThat(조회결과).extracting(IndividualQuestionsResponse::getLike)
                .containsAll(List.of(true, true, false));
    }

    @ParameterizedTest
    @MethodSource("수정항목_설정하기")
    @DisplayName("질문의 내용, 순서, 중요도를 하나의 메서드로 수정한다")
    void 질문_수정하기(QuestionUpdateType 수정항목, Object 수정내용) {
        // given
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");
        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");
        IndividualQuestion 질문 = environ.테스트개별질문_저장하기(지원자A, "자기소개해주세요", 3, true, 3, null);

        QuestionUpdateMessage 수정요청 = new QuestionUpdateMessage(수정내용, 수정항목);

        // when
        questionService.updateQuestion(질문.getId(), 수정요청);

        // then
        IndividualQuestion 수정된질문 = questionRepository.findById(질문.getId()).get();
        log.info("수정된 질문 | 내용: {}, 중요도: {}, 순서: {}", 수정된질문.getContent(), 수정된질문.getImportance(), 수정된질문.getQuestionOrder());
        assertTrue(수정내용.equals(수정된질문.getContent()) ||
                        수정내용.equals(수정된질문.getQuestionOrder()) ||
                        수정내용.equals(수정된질문.getImportance()));
    }

    static Stream<Arguments> 수정항목_설정하기() {
        return Stream.of(
                Arguments.arguments(QuestionUpdateType.CONTENT, "하이"),
                Arguments.arguments(QuestionUpdateType.IMPORTANCE, 5),
                Arguments.arguments(QuestionUpdateType.ORDER, 5)
        );
    }

    @Test
    @DisplayName("지원자에 저장된 질문 공개 여부에 따라 개별 질문의 공개 여부를 변경한다.")
    void 질문_공개여부_변경하기() {
        // given
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");
        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");
        IndividualQuestion 질문 = environ.테스트개별질문_저장하기(지원자A, "자기소개해주세요", 3, true, 3, null);

        environ.테스트지원자_질문공개하기(지원자A);

        // when
        questionService.listInterviewQuestions(지원자A.getId());

        // then
        IndividualQuestion 수정된질문 = questionRepository.findById(질문.getId()).get();
        assertEquals(QuestionPublicType.PENDING, 질문.getPublicType());
        assertEquals(QuestionPublicType.PUBLIC, 수정된질문.getPublicType());
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

        IndividualQuestion 질문A = environ.테스트개별질문_저장하기(지원자A, "자기소개해주세요.", 1, true, 5, 종미);
        IndividualQuestion 질문B = environ.테스트개별질문_저장하기(지원자A, "장점을소개해주세요.", 2, true, 5, 종미);

        environ.테스트평가_저장하기(1, "인상이좋다", 질문A, 종미);
        environ.테스트평가_저장하기(2, "굿", 질문A, 윤정);
        environ.테스트평가_저장하기(3, "좋은장점이다", 질문B, 종미);
        environ.테스트평가_저장하기(4, "좋다", 질문B, 윤정);

        // when
        List<QuestionRankResponse> 조회결과 = questionService.findQuestionRanks(지원자A.getId());

        // then
        assertThat(조회결과).hasSize(2)
                .extracting(QuestionRankResponse::totalScore)
                .containsExactlyElementsOf(List.of(17.5, 7.5));
    }

    @Test
    @DisplayName("면접 질문 확인 시 이벤트가 발생한다.")
    void 면접질문_준비_이벤트_발생시키기() {
        // given
        Project 테스트프로젝트 = environ.테스트프로젝트_저장하기();
        Interview 테스트면접 = environ.테스트면접_저장하기(테스트프로젝트, "테스트면접");
        Applicant 지원자A = environ.테스트지원자_저장하기(테스트면접, "지원자A");

        // when
        questionService.listPreparatoryQuestions(지원자A.getId());

        // then
        assertEquals(1L, events.stream(QuestionPreparedEvent.class).count());
    }
}
