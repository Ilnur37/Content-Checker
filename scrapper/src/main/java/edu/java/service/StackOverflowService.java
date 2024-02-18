package edu.java.service;

import edu.java.dto.stackoverflow.QuestionInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class StackOverflowService {
    private final WebClient stackOverflowClient;

    public StackOverflowService(WebClient stackOverflowClient) {
        this.stackOverflowClient = stackOverflowClient;
    }

    public QuestionInfo getQuestionInfo(Long question) {
        return stackOverflowClient.get()
            .uri("/questions/{question}/answers?order=desc&site=stackoverflow", question)
            .retrieve()
            .bodyToMono(QuestionInfo.class)
            .block();
    }
}
