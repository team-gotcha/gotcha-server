package com.gotcha.server.question.dto.response;

import com.gotcha.server.question.domain.IndividualQuestion;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndividualQuestionsResponse {
    private Long id;
    private String content;
    private String writerName;
    private String writerProfile;
    private Long commentTargetId;
    private Boolean asking;
    private Boolean like;
    private Long likeCount;

    public static IndividualQuestionsResponse from(
            final IndividualQuestion individualQuestion, final Boolean like, final Long likeCount) {
        Long commentTargetId = null;
        IndividualQuestion commentTarget = individualQuestion.getCommentTarget();
        if(Objects.nonNull(commentTarget)) {
            commentTargetId = commentTarget.getId();
        }

        return IndividualQuestionsResponse.builder()
                .id(individualQuestion.getId())
                .content(individualQuestion.getContent())
                .writerName(individualQuestion.getMember().getName())
                .writerProfile(individualQuestion.getMember().getProfileUrl())
                .commentTargetId(commentTargetId)
                .asking(individualQuestion.isAsking())
                .like(like)
                .likeCount(likeCount)
                .build();
    }
}
