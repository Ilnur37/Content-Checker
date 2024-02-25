package edu.java.scrapper.client;

import edu.java.scrapper.dto.stackoverflow.answer.AnswerInfo;
import edu.java.scrapper.dto.stackoverflow.answer.AnswerItems;
import edu.java.scrapper.dto.stackoverflow.question.QuestionInfo;
import edu.java.scrapper.dto.stackoverflow.question.QuestionItems;
import java.util.List;
import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient extends Client {
    public StackOverflowClient(WebClient webClient) {
        super(webClient);
    }

    public static StackOverflowClient create(String baseUrl) {
        WebClient webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();

        return new StackOverflowClient(webClient);
    }

    public QuestionInfo getQuestionInfo(Long question) {
        return Objects.requireNonNull(webClient.get()
                .uri("/questions/{question}?order=desc&sort=activity&site=stackoverflow", question)
                .retrieve()
                .bodyToMono(QuestionItems.class)
                .block())
            .getQuestionInfo()
            .getFirst();
    }

    public List<AnswerInfo> getAnswerInfoByQuestion(Long question) {
        return Objects.requireNonNull(webClient.get()
                .uri("/questions/{question}/answers?order=desc&site=stackoverflow", question)
                .retrieve()
                .bodyToMono(AnswerItems.class)
                .block())
            .getAnswerInfo();
    }
}
