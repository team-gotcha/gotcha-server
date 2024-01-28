package com.gotcha.server.mongo.repository;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.mongo.domain.ApplicantMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicantMongoRepository extends MongoRepository<ApplicantMongo, String> {
    Optional<ApplicantMongo> findByApplicantId(Long applicantId);
}
