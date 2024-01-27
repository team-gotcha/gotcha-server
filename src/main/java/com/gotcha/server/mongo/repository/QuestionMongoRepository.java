package com.gotcha.server.mongo.repository;

import com.gotcha.server.mongo.domain.QuestionMongo;
import java.math.BigInteger;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionMongoRepository extends MongoRepository<QuestionMongo, BigInteger> {

}
