package com.gotcha.server.evaluation.domain;

import com.gotcha.server.question.dto.response.QuestionRankResponse;
import com.gotcha.server.question.domain.IndividualQuestion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuestionEvaluations {
    private final Map<IndividualQuestion, Double> totalQuestionsScore;

    public QuestionEvaluations(final List<IndividualQuestion> questions) {
        this.totalQuestionsScore = calculateTotalQuestionsScore(questions);
    }

    public Map<IndividualQuestion, Double> calculateTotalQuestionsScore(final List<IndividualQuestion> questions) {
        return questions.stream()
                .collect(Collectors.toMap(
                        question -> question,
                        question -> question.calculateEvaluationScore()));
    }

    public double calculatePerfectScore(final List<IndividualQuestion> questions) {
        BigDecimal totalSum = BigDecimal.valueOf(questions.stream()
                .mapToDouble(question -> question.calculatePerfectScore().doubleValue()) // BigDecimal을 double로 변환
                .sum());

        return totalSum.divide(BigDecimal.valueOf(questions.size())).doubleValue();
    }

    public List<IndividualQuestion> calculateQuestionsRank() {
        List<IndividualQuestion> ranks = new ArrayList<>(totalQuestionsScore.keySet());
        ranks.sort((questionId1, questionId2) ->
                        totalQuestionsScore.get(questionId2).compareTo(totalQuestionsScore.get(questionId1)));
        return ranks;
    }

    public List<QuestionRankResponse> createQuestionRanks() {
        List<IndividualQuestion> ranks = calculateQuestionsRank();
        return ranks.stream()
                .map(question -> new QuestionRankResponse(question.getId(), totalQuestionsScore.get(question)))
                .toList();
    }


}
