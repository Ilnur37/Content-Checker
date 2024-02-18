package edu.java.service;

import edu.java.dto.stackoverflow.answer.AnswerInfo;
import edu.java.dto.stackoverflow.answer.AnswerItems;
import edu.java.dto.stackoverflow.question.QuestionInfo;
import edu.java.dto.stackoverflow.question.QuestionItems;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class StackOverflowService {
    private final WebClient stackOverflowClient;

    public StackOverflowService(WebClient stackOverflowClient) {
        this.stackOverflowClient = stackOverflowClient;
    }

    public QuestionInfo getQuestionInfo(Long question) {
        return Objects.requireNonNull(stackOverflowClient.get()
                .uri("/questions/{question}?order=desc&sort=activity&site=stackoverflow", question)
                .retrieve()
                .bodyToMono(QuestionItems.class)
                .block())
            .getQuestionInfo()
            .getFirst();
    }

    public List<AnswerInfo> getAnswerInfoByQuestion(Long question) {
        return Objects.requireNonNull(stackOverflowClient.get()
                .uri("/questions/{question}/answers?order=desc&site=stackoverflow", question)
                .retrieve()
                .bodyToMono(AnswerItems.class)
                .block())
            .getAnswerInfo();
    }

}
