package com.gotcha.server.applicant.domain;

import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;

public enum InterviewStatus {
    PREPARATION,
    IN_PROGRESS,
    COMPLETION;

    public InterviewStatus moveToNextStatus() {
        if(this.equals(PREPARATION)) {
            return IN_PROGRESS;
        } else if(this.equals(IN_PROGRESS)) {
            return COMPLETION;
        }
        throw new AppException(ErrorCode.INVALID_INTERVIEW_STATUS);
    }
}
