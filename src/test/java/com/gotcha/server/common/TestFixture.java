package com.gotcha.server.common;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;

public class TestFixture {
    public static final Member 종미면접관 = Member.builder().socialId("jongmi social id").email("jongmi email").name("종미").build();
    public static final Project 테스트프로젝트 = Project.builder().name("테스트면접").teamName("CEOS").build();
    public static final Interview 테스트면접 = Interview.builder().project(테스트프로젝트).name("테스트면접").build();
    public static final Applicant 지원자A = new Applicant(테스트면접);
    public static final Applicant 지원자B = new Applicant(테스트면접);
    public static final Applicant 지원자C = new Applicant(테스트면접);
    public static final Interviewer 종미면접관_지원자A = new Interviewer(지원자A, 종미면접관);
    public static final Interviewer 종미면접관_지원자B = new Interviewer(지원자B, 종미면접관);
    public static final Interviewer 종미면접관_지원자C = new Interviewer(지원자C, 종미면접관);
}
