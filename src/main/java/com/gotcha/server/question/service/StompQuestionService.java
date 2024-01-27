package com.gotcha.server.question.service;

import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.mongo.domain.QuestionMongo;
import com.gotcha.server.mongo.repository.QuestionMongoRepository;
import com.gotcha.server.question.dto.message.QuestionUpdateMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StompQuestionService {
    private final QuestionMongoRepository questionMongoRepository;

    @Transactional
    public void updateQuestion(final Long questionId, final QuestionUpdateMessage message) {
        QuestionMongo question = questionMongoRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUNT));
        QuestionUpdateType updateType = message.type();
        updateType.update(question, message.value());
    }
}
