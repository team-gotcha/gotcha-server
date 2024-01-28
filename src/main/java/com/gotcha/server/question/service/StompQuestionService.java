package com.gotcha.server.question.service;

import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.mongo.domain.QuestionMongo;
import com.gotcha.server.mongo.repository.QuestionMongoRepository;
import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.dto.message.QuestionUpdateMessage;
import com.gotcha.server.question.event.QuestionDeterminedEvent;
import com.gotcha.server.question.event.QuestionPreparedEvent;
import com.gotcha.server.question.event.QuestionUpdatedEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StompQuestionService {
    private final QuestionMongoRepository questionMongoRepository;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener(QuestionPreparedEvent.class)
    public void migrateQuestions(final QuestionPreparedEvent event) {
        Long applicantId = event.applicantId();
        List<IndividualQuestion> individualQuestions = event.individualQuestions();
        List<QuestionMongo> mongoQuestions = individualQuestions.stream()
                .map(q -> QuestionMongo.from(q, applicantId)).toList();
        questionMongoRepository.saveAll(mongoQuestions);
    }

    public void updateQuestion(final Long questionId, final QuestionUpdateMessage message) {
        QuestionMongo question = questionMongoRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUNT));
        QuestionUpdateType updateType = message.type();
        updateType.update(question, message.value());
        questionMongoRepository.save(question);
    }

    @EventListener(QuestionDeterminedEvent.class)
    public void findAllModifiedQuestions(final QuestionDeterminedEvent event) {
        List<QuestionMongo> mongoQuestions = questionMongoRepository.findAllByApplicantId(event.applicantId());
        eventPublisher.publishEvent(new QuestionUpdatedEvent(mongoQuestions));
    }
}
