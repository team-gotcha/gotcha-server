package com.gotcha.server.mongo.service;

import com.gotcha.server.applicant.dto.message.OutcomeUpdateMessage;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.mongo.domain.ApplicantMongo;
import com.gotcha.server.mongo.repository.ApplicantMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicantMongoService {

    private final ApplicantMongoRepository applicantMongoRepository;

    public void updateMongoOutcome(Long applicantId, OutcomeUpdateMessage message) {
        final ApplicantMongo applicant = applicantMongoRepository.findByApplicantId(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));

        applicant.updateOutCome(message.value());
        applicantMongoRepository.save(applicant);
    }
}
