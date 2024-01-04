package com.gotcha.server.project.service;

import com.gotcha.server.project.domain.Collaborator;
import com.gotcha.server.project.domain.Interview;
import com.gotcha.server.project.domain.Project;
import com.gotcha.server.project.domain.Subcollaborator;
import com.gotcha.server.project.dto.request.InterviewRequest;
import com.gotcha.server.project.dto.request.ProjectRequest;
import com.gotcha.server.project.dto.response.ProjectResponse;
import com.gotcha.server.project.repository.CollaboratorRepository;
import com.gotcha.server.project.repository.InterviewRepository;
import com.gotcha.server.project.repository.ProjectRepository;
import com.gotcha.server.project.repository.SubcollaboratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final SubcollaboratorRepository subcollaboratorRepository;

    public void createInterview(InterviewRequest request){
        Interview interview = request.toEntity();
        interviewRepository.save(interview);
        createSubcollaborator(interview, request.getEmails());
    }

    public void createSubcollaborator(Interview interview, List<String> emails){
        for(String email : emails){
            Subcollaborator subcollaborator = Subcollaborator.builder()
                    .email(email)
                    .interview(interview)
                    .build();
            subcollaboratorRepository.save(subcollaborator);
        }
    }
}
