package com.gotcha.server.mongo.repository;

import com.gotcha.server.mongo.domain.QuestionMongo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionMongoRepository extends MongoRepository<QuestionMongo, String> {
    Optional<QuestionMongo> findByQuestionId(Long questionId);
    List<QuestionMongo> findAllByApplicantId(Long applicantId);
    List<QuestionMongo> findAllByApplicantIdAndAskingOrderByQuestionOrderAsc(Long applicantId, Boolean asking);
}
