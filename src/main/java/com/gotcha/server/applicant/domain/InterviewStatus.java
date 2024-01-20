package com.gotcha.server.applicant.domain;

import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;

public enum InterviewStatus {
    PREPARATION, // 면접 진행 전
    IN_PROGRESS, // 면접 진행 중
    COMPLETION, // 면접 진행 완료
    ANNOUNCED; // 면접 발표 완료

    public InterviewStatus moveToNextStatus() {
        if(this.equals(PREPARATION)) {
            return IN_PROGRESS;
        } else if(this.equals(IN_PROGRESS)) {
            return COMPLETION;
        } else if(this.equals(COMPLETION)) {
            return ANNOUNCED;
        }
        throw new AppException(ErrorCode.INVALID_INTERVIEW_STATUS);
    }
}
