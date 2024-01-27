package com.gotcha.server.question.service;

import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.mongo.domain.QuestionMongo;
import com.gotcha.server.mongo.repository.QuestionMongoRepository;
import com.gotcha.server.question.domain.IndividualQuestion;
import com.gotcha.server.question.dto.message.QuestionUpdateMessage;
import com.gotcha.server.question.event.QuestionPreparedEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StompQuestionService {
    private final QuestionMongoRepository questionMongoRepository;

    @Transactional
    @EventListener(QuestionPreparedEvent.class)
    public void migrateQuestions(final QuestionPreparedEvent event) {
        Long applicantId = event.applicantId();
        List<IndividualQuestion> individualQuestions = event.individualQuestions();
        List<QuestionMongo> mongoQuestions = individualQuestions.stream()
                .map(q -> QuestionMongo.from(q, applicantId)).toList();
        questionMongoRepository.saveAll(mongoQuestions);
    }

    @Transactional
    public void updateQuestion(final Long questionId, final QuestionUpdateMessage message) {
        QuestionMongo question = questionMongoRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUNT));
        QuestionUpdateType updateType = message.type();
        updateType.update(question, message.value());
    }
}
