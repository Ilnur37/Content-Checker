package edu.java.scrapper.service;

import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.dto.stackoverflow.answer.AnswerInfo;
import edu.java.scrapper.dto.stackoverflow.question.QuestionInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackOverflowService {
    private final StackOverflowClient stackOverflowClient;

    public QuestionInfo getQuestionInfo(Long question) {
        return stackOverflowClient.getQuestionInfo(question);
    }

    public List<AnswerInfo> getAnswerInfoByQuestion(Long question) {
        return stackOverflowClient.getAnswerInfoByQuestion(question);
    }

}
