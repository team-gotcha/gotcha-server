package com.gotcha.server.project.repository;

import com.gotcha.server.member.domain.Member;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;

import java.util.List;

public interface InterviewDslRepository {
    List<Interview> getInterviewList(String email, Project project);
    List<Member> getInterviewerList(Interview interview);
}
