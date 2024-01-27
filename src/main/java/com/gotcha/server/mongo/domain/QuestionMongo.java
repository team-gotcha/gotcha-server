package com.gotcha.server.mongo.domain;

import java.math.BigInteger;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "questions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class QuestionMongo {
    @Id
    private String id;
    private Long questionId;
    private String content;
    private Integer importance;
    private Integer questionOrder;
    private Boolean isCommon;
    private BigInteger applicantId;
    private BigInteger writerId;
}
