package com.gotcha.server.common;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.applicant.domain.Keyword;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;

public class TestFixture {
    public static final Member 종미면접관 = Member.builder().socialId("jongmi social id").email("jongmi email").name("종미").profileUrl("jongmi profile").build();
    public static final Member 윤정면접관 = Member.builder().socialId("yunjeong social id").email("yunjeong email").name("윤정").profileUrl("yunjeong profile").build();
    public static final Project 테스트프로젝트 = Project.builder().name("테스트면접").teamName("CEOS").build();
    public static final Interview 테스트면접 = Interview.builder().project(테스트프로젝트).name("테스트면접").build();
    public static final Interview 테스트면접2 = Interview.builder().project(테스트프로젝트).name("테스트면접2").build();
    public static final Applicant 지원자A = new Applicant(테스트면접);
    public static final Applicant 지원자B = new Applicant(테스트면접);
    public static final Applicant 지원자C = new Applicant(테스트면접);
    public static final Applicant 지원자D = new Applicant(테스트면접2);
    public static final Interviewer 종미면접관_지원자A = new Interviewer(지원자A, 종미면접관);
    public static final Interviewer 종미면접관_지원자B = new Interviewer(지원자B, 종미면접관);
    public static final Interviewer 종미면접관_지원자C = new Interviewer(지원자C, 종미면접관);
    public static final Interviewer 윤정면접관_지원자A = new Interviewer(지원자A, 윤정면접관);
    public static final Keyword 지원자A_성실함 = new Keyword(지원자A, "성실함", KeywordType.TRAIT);
    public static final Keyword 지원자A_인턴경험 = new Keyword(지원자A, "인턴경험", KeywordType.EXPERIENCE);
    public static final Keyword 지원자A_SQL자격증 = new Keyword(지원자A, "SQL자격증", KeywordType.SKILL);
    public static final Keyword 지원자B_성실함 = new Keyword(지원자B, "성실함", KeywordType.TRAIT);
    public static final Keyword 지원자B_깔끔함 = new Keyword(지원자B, "깔끔함", KeywordType.TRAIT);
}
