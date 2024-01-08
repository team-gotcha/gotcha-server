package com.gotcha.server.common;

import static com.gotcha.server.common.TestFixture.*;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.applicant.domain.Keyword;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.applicant.repository.ApplicantRepository;
import com.gotcha.server.applicant.repository.InterviewerRepository;
import com.gotcha.server.applicant.repository.KeywordRepository;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.member.repository.MemberRepository;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.repository.InterviewRepository;
import com.gotcha.server.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IntegrationTestEnviron {
    private final MemberRepository memberRepository;
    private final ApplicantRepository applicantRepository;
    private final ProjectRepository projectRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewerRepository interviewerRepository;
    private final KeywordRepository keywordRepository;

    public Member 테스트유저_저장하기(String 이름) {
        return memberRepository.save(테스트유저(이름));
    }

    public Project 테스트프로젝트_저장하기() {
        return projectRepository.save(테스트프로젝트());
    }

    public Interview 테스트면접_저장하기(Project 프로젝트, String 면접이름) {
        return interviewRepository.save(테스트면접(프로젝트, 면접이름));
    }

    public Applicant 테스트지원자_저장하기(Interview 면접, String 이름) {
        return applicantRepository.save(테스트지원자(면접, 이름));
    }

    public Interviewer 테스트면접관_저장하기(Applicant 지원자, Member 면접관) {
        return interviewerRepository.save(테스트면접관(지원자, 면접관));
    }

    public Keyword 테스트키워드_저장하기(Applicant 지원자, String 내용, KeywordType 종류) {
        return keywordRepository.save(테스트키워드(지원자, 내용, 종류));
    }
}
