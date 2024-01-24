package com.gotcha.server.common;

import com.gotcha.server.applicant.domain.Applicant;
import com.gotcha.server.applicant.domain.Favorite;
import com.gotcha.server.applicant.domain.Interviewer;
import com.gotcha.server.applicant.domain.Keyword;
import com.gotcha.server.applicant.domain.KeywordType;
import com.gotcha.server.evaluation.domain.Evaluation;
import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Collaborator;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.domain.Subcollaborator;
import com.gotcha.server.question.domain.CommonQuestion;
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
        return Applicant.builder()
                .name(이름)
                .interview(면접)
                .email(String.format("%s email", 이름))
                .build();
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

    public static IndividualQuestion 테스트개별질문(Applicant 지원자, String 내용, Integer 순서, boolean 면접때질문하기, Integer 중요도, Member 작성자) {
        IndividualQuestion question = IndividualQuestion.builder()
                .applicant(지원자)
                .content(내용)
                .member(작성자)
                .build();
        question.updateOrder(순서);
        question.updateImportance(중요도);
        if(면접때질문하기) {
            question.askDuringInterview();
        }
        return question;
    }

    public static CommonQuestion 테스트공통질문(Interview 면접, String 내용) {
        return new CommonQuestion(내용, 면접);
    }

    public static Evaluation 테스트평가(Integer 점수, String 평가내용, IndividualQuestion 질문, Member 면접관) {
        return Evaluation.builder()
                .score(점수)
                .content(평가내용)
                .member(면접관)
                .question(질문)
                .build();
    }

    public static Favorite 테스트즐겨찾기(Applicant 지원자, Member 면접관) {
        return new Favorite(면접관, 지원자);
    }
}
