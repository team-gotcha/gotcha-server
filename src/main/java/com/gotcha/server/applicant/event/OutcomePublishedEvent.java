package com.gotcha.server.applicant.event;

import com.gotcha.server.applicant.domain.Applicant;

public record OutcomePublishedEvent(Applicant applicant, String projectName, String interviewName, String positionName) {

}
