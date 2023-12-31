package com.gotcha.server.common;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.applicant.domain.Keyword;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Collaborator;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.domain.Subcollaborator;
import com.gotcha.server.question.domain.IndividualQuestion;

public class TestFixture {
    public static Member 테스트유저(String 이름) {
        return Member.builder()
                .socialId(String.format("%s social id", 이름))
                .email(String.format("%s email", 이름))
                .name(이름)
                .profileUrl(String.format("%s profile", 이름))
                .build();
    }

    public static Project 테스트프로젝트() {
        return Project.builder().name("테스트프로젝트").build();
    }

    public static Interview 테스트면접(Project 프로젝트, String 면접이름) {
        return Interview.builder().project(프로젝트).name(면접이름).build();
    }

    public static Applicant 테스트지원자(Interview 면접, String 이름) {
        Applicant applicant = new Applicant(면접);
        applicant.setName(이름);
        return applicant;
    }

    public static Interviewer 테스트면접관(Applicant 지원자, Member 면접관) {
        return new Interviewer(지원자, 면접관);
    }

    public static Keyword 테스트키워드(Applicant 지원자, String 내용, KeywordType 종류) {
        return new Keyword(지원자, 내용, 종류);
    }

    public static Collaborator 테스트콜라보레이터(String 이메일, Project 프로젝트){
        return new Collaborator(이메일, 프로젝트);
    }

    public static Subcollaborator 테스트서브콜라보레이터(String 이메일, Interview 인터뷰){
        return new Subcollaborator(이메일, 인터뷰);
    }

    public static IndividualQuestion 테스트개별질문(Applicant 지원자, String 내용, Integer 순서) {
        IndividualQuestion question = IndividualQuestion.builder()
                .applicant(지원자)
                .content(내용)
                .build();
        question.setQuestionOrder(순서);
        return question;
    }
}
