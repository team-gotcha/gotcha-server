package com.gotcha.server.applicant.controller;

import com.gotcha.server.applicant.dto.message.OutcomeUpdateMessage;
import com.gotcha.server.applicant.service.ApplicantService;
import com.gotcha.server.global.aop.ExeTimer;
import com.gotcha.server.mongo.service.ApplicantMongoService;
import com.gotcha.server.question.dto.message.QuestionUpdateMessage;
import com.gotcha.server.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompApplicantController {
    private final ApplicantMongoService applicantMongoService;
    private final ApplicantService applicantService;

    @MessageMapping("/applicant/{applicant-id}")
    @SendTo("/sub/applicant/{applicant-id}")
    @Operation(description = "실시간으로 지원자의 합격 여부를 수정한다.")
    @ExeTimer
    public OutcomeUpdateMessage updateOutcome(
            @DestinationVariable(value = "applicant-id") Long applicantId, @Payload OutcomeUpdateMessage message) {
        applicantService.updateOutcome(applicantId, message);
        return message;
    }
}
