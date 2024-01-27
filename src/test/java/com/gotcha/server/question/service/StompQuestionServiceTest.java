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
import com.gotcha.server.question.event.QuestionPreparedEvent;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class StompQuestionServiceTest extends IntegrationTest {
    @Autowired
    private StompQuestionService stompQuestionService;

    @Autowired
    private QuestionMongoRepository questionMongoRepository;

    @Test
    void MongDB로_질문목록_이전하기() {
        // given
        Project 조회할프로젝트 = 테스트프로젝트("테스트프로젝트");
        Interview 조회할면접 = 테스트면접(조회할프로젝트, "테스트면접1");
        Applicant 지원자A = 테스트지원자(조회할면접, "지원자A");
        List<IndividualQuestion> 질문목록 = List.of(
                테스트개별질문(지원자A, "내용1", 1, true, 5, null),
                테스트개별질문(지원자A, "내용2", 2, true, 5, null));

        // when
        QuestionPreparedEvent 이벤트 = new QuestionPreparedEvent(질문목록, 지원자A.getId());
        stompQuestionService.migrateQuestions(이벤트);

        // then
        List<QuestionMongo> 이전된_질문목록 = questionMongoRepository.findAll();
        assertThat(이전된_질문목록).hasSize(2)
                .extracting(QuestionMongo::getContent)
                .contains("내용1", "내용2");
    }
}