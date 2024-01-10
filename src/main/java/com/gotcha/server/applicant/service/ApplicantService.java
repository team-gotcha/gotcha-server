package com.gotcha.server.applicant.service;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.applicant.domain.Keyword;
import com.gotcha.server.applicant.domain.PreparedInterviewer;
import com.gotcha.server.applicant.dto.request.InterviewProceedRequest;
import com.gotcha.server.applicant.dto.response.ApplicantResponse;
import com.gotcha.server.applicant.dto.response.ApplicantsResponse;
import com.gotcha.server.applicant.dto.response.InterviewProceedResponse;
import com.gotcha.server.applicant.dto.response.TodayInterviewResponse;
import com.gotcha.server.applicant.repository.ApplicantRepository;
import com.gotcha.server.applicant.repository.InterviewerRepository;
import com.gotcha.server.applicant.repository.KeywordRepository;
import com.gotcha.server.applicant.repository.PreparedInterviewerRepository;
import com.gotcha.server.auth.security.MemberDetails;
import com.gotcha.server.global.exception.AppException;
import com.gotcha.server.global.exception.ErrorCode;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.repository.InterviewRepository;
import java.util.List;
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
    private final InterviewRepository interviewRepository;
    private final KeywordRepository keywordRepository;

    @Transactional
    public InterviewProceedResponse proceedToInterview(final InterviewProceedRequest request, final MemberDetails details) {
        Applicant applicant = applicantRepository.findById(request.applicantId())
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        createNewPreparedInterviewer(applicant, details.member());

        long interviewerCount = interviewerRepository.countByApplicant(applicant);
        long preparedInterviewerCount = preparedInterviewerRepository.countByApplicant(applicant);
        if(interviewerCount <= preparedInterviewerCount) {
            applicant.moveToNextStatus();
        }
        return new InterviewProceedResponse(interviewerCount, preparedInterviewerCount);
    }

    private void createNewPreparedInterviewer(final Applicant applicant, final Member member) {
        Interviewer interviewer = interviewerRepository.findByMember(member)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED_INTERVIEWER));
        if(!preparedInterviewerRepository.existsByInterviewer(interviewer)) {
            preparedInterviewerRepository.save(new PreparedInterviewer(applicant, interviewer));
        }
    }

    public TodayInterviewResponse countTodayInterview(final MemberDetails details) {
        long count = interviewerRepository.countTodayInterview(details.member());
        return new TodayInterviewResponse(count);
    }

    public List<ApplicantsResponse> listApplicantsByInterview(final Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new AppException(ErrorCode.INTERVIEW_NOT_FOUNT));
        return applicantRepository.findAllByInterviewWithKeywords(interview);
    }

    public ApplicantResponse findApplicantDetailsById(final Long applicantId) {
        Applicant applicant = applicantRepository.findByIdWithInterviewer(applicantId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUNT));
        List<Keyword> keywords = keywordRepository.findAllByApplicant(applicant);
        return ApplicantResponse.from(applicant, keywords);
    }
}
