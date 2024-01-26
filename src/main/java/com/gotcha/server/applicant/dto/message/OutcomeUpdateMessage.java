package com.gotcha.server.applicant.dto.message;

import com.gotcha.server.applicant.domain.Outcome;

public record OutcomeUpdateMessage(Outcome value) {
}