package com.gotcha.server.question.service;

import static com.gotcha.server.common.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.common.IntegrationTest;
import com.gotcha.server.mongo.domain.QuestionMongo;
import com.gotcha.server.mongo.repository.QuestionMongoRepository;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.dto.response.InterviewQuestionResponse;
import com.gotcha.server.question.event.QuestionPreparedEvent;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class QuestionMongoServiceTest extends IntegrationTest {
    @Autowired
    private QuestionMongoService questionMongoService;

    @Autowired
    private QuestionMongoRepository questionMongoRepository;

    @Test
    void MongDB로_질문목록_이전하기() {
        // given
        Project 조회할프로젝트 = 테스트프로젝트("테스트프로젝트");
        Interview 조회할면접 = 테스트면접(조회할프로젝트, "테스트면접1");
        Applicant 지원자A = 테스트지원자(조회할면접, "지원자A", LocalDate.now());
        List<IndividualQuestion> 질문목록 = List.of(
                테스트개별질문(지원자A, "내용1", 1, true, 5, null),
                테스트개별질문(지원자A, "내용2", 2, true, 5, null));

        // when
        QuestionPreparedEvent 이벤트 = new QuestionPreparedEvent(질문목록, 지원자A.getId());
        questionMongoService.migrateQuestions(이벤트);

        // then
        List<QuestionMongo> 이전된_질문목록 = questionMongoRepository.findAll();
        assertThat(이전된_질문목록).hasSize(2)
                .extracting(QuestionMongo::getContent)
                .contains("내용1", "내용2");
    }

    @Test
    @DisplayName("면접 중 정해진 순서대로 지원자의 질문 목록을 조회한다.")
    void 면접중_질문목록_조회하기() {
        // given
        Long 지원자ID = 1L;
        QuestionMongo 개별질문A = 개별질문_생성하기(4, 1L, 지원자ID);
        QuestionMongo 개별질문B = 개별질문_생성하기(3, 2L, 지원자ID);
        QuestionMongo 개별질문C = 개별질문_생성하기(2, 3L, 지원자ID);
        QuestionMongo 개별질문D = 개별질문_생성하기(1, 4L, 지원자ID);
        questionMongoRepository.saveAll(List.of(개별질문A, 개별질문B, 개별질문C, 개별질문D));

        // when
        List<InterviewQuestionResponse> 조회결과 = questionMongoService.findAllDuringInterview(지원자ID);

        // then
        assertThat(조회결과).hasSize(4)
                .extracting(InterviewQuestionResponse::getId)
                .containsExactly(4L, 3L, 2L, 1L);
    }

    private QuestionMongo 개별질문_생성하기(int 순서, Long 질문ID, long 지원자ID) {
        return QuestionMongo.builder()
                .questionId(질문ID)
                .content(질문ID.toString())
                .importance(2)
                .questionOrder(순서)
                .isCommon(false)
                .applicantId(지원자ID)
                .build();
    }
}