package edu.java.scrapper.client;

import edu.java.scrapper.dto.stackoverflow.answer.AnswerInfo;
import edu.java.scrapper.dto.stackoverflow.answer.AnswerItems;
import edu.java.scrapper.dto.stackoverflow.comment.CommentInfo;
import edu.java.scrapper.dto.stackoverflow.comment.CommentItems;
import edu.java.scrapper.dto.stackoverflow.question.QuestionInfo;
import edu.java.scrapper.dto.stackoverflow.question.QuestionItems;
import java.util.List;
import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient extends Client {
    private static final String GET_ANSWER_INFO_BY_QUESTION_URL =
        "/questions/{question}/answers?order=desc&site=stackoverflow";
    private static final String GET_COMMENT_INFO_BY_QUESTION_URL =
        "/questions/{question}/comments?order=desc&sort=creation&site=stackoverflow";

    public StackOverflowClient(WebClient webClient) {
        super(webClient);
    }

    public static StackOverflowClient create(String baseUrl) {
        WebClient webClient = WebClient.create(baseUrl);
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
                .uri(GET_ANSWER_INFO_BY_QUESTION_URL, question)
                .retrieve()
                .bodyToMono(AnswerItems.class)
                .block())
            .getAnswerInfo();
    }

    public List<CommentInfo> getCommentInfoByQuestion(Long question) {
        return Objects.requireNonNull(webClient.get()
                .uri(GET_COMMENT_INFO_BY_QUESTION_URL, question)
                .retrieve()
                .bodyToMono(CommentItems.class)
                .block())
            .getCommentInfo();
    }
}
