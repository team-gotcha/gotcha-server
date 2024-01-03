package com.gotcha.server.applicant.service;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.applicant.domain.PreparedInterviewer;
import com.gotcha.server.applicant.dto.request.InterviewProceedRequest;
import com.gotcha.server.applicant.dto.response.InterviewProceedResponse;
import com.gotcha.server.applicant.dto.response.TodayInterviewResponse;
import com.gotcha.server.applicant.repository.ApplicantRepository;
import com.gotcha.server.applicant.repository.InterviewerRepository;
import com.gotcha.server.applicant.repository.PreparedInterviewerRepository;
import com.gotcha.server.auth.security.MemberDetails;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final InterviewerRepository interviewerRepository;
    private final PreparedInterviewerRepository preparedInterviewerRepository;

    @Transactional
    public InterviewProceedResponse proceedToInterview(final InterviewProceedRequest request, final MemberDetails details) {
        Applicant applicant = applicantRepository.findById(request.applicantId())
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        Interviewer interviewer = interviewerRepository.findByMember(details.member())
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED_INTERVIEWER));
        if(!preparedInterviewerRepository.existsByInterviewer(interviewer)) {
            preparedInterviewerRepository.save(new PreparedInterviewer(applicant, interviewer));
        }

        long interviewerCount = interviewerRepository.countByApplicant(applicant);
        long preparedInterviewerCount = preparedInterviewerRepository.countByApplicant(applicant);
        if(interviewerCount <= preparedInterviewerCount) {
            applicant.moveToNextStatus();
        }
        return new InterviewProceedResponse(interviewerCount, preparedInterviewerCount);
    }

    public TodayInterviewResponse countTodayInterview(final MemberDetails details) {
        long count = interviewerRepository.countTodayInterview(details.member());
        return new TodayInterviewResponse(count);
    }
}
