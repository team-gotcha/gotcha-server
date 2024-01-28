package com.gotcha.server.applicant.event;

import com.gotcha.server.applicant.domain.Applicant;

public record InterviewStartedEvent(Applicant applicant) {

}
