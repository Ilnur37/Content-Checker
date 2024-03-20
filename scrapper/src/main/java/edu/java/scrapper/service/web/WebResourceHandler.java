package edu.java.scrapper.service.web;

import edu.java.models.exception.InvalidUrlException;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.github.ActionsInfo;
import edu.java.scrapper.dto.github.RepositoryInfo;
import edu.java.scrapper.dto.stackoverflow.answer.AnswerInfo;
import edu.java.scrapper.dto.stackoverflow.comment.CommentInfo;
import edu.java.scrapper.dto.stackoverflow.question.QuestionInfo;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class WebResourceHandler {
    private static final String URL = "url: ";
    private static final String SPLIT_URL = "/";
    private final ApplicationConfig.Supported api;
    private final StackOverflowService stackOverflowService;
    private final GitHubService gitHubService;

    public WebResourceHandler(
        ApplicationConfig appConf,
        StackOverflowService stackOverflowService,
        GitHubService gitHubService
    ) {
        this.api = appConf.supported();
        this.stackOverflowService = stackOverflowService;
        this.gitHubService = gitHubService;
    }

    public boolean isGitHubUrl(String url) {
        return url.contains(api.github());
    }

    public boolean isStackOverflowUrl(String url) {
        return url.contains(api.stackoverflow());
    }

    public RepositoryInfo getRepositoryGitHubInfoByUrl(String url) {
        String[] urlParts = url.split(SPLIT_URL);
        String owner = urlParts[urlParts.length - 2];
        String repo = urlParts[urlParts.length - 1];
        try {
            return gitHubService.getRepositoryInfo(owner, repo);
        } catch (WebClientResponseException ex) {
            throw new InvalidUrlException(URL + url);
        }
    }

    public List<ActionsInfo> getActionsGitHubInfoByUrl(String url) {
        String[] urlParts = url.split(SPLIT_URL);
        String owner = urlParts[urlParts.length - 2];
        String repo = urlParts[urlParts.length - 1];
        return gitHubService.getActionsInfo(owner, repo);
    }

    public List<AnswerInfo> getAnswersStackOverflowByUrl(String url) {
        String[] urlParts = url.split(SPLIT_URL);
        Long question = Long.valueOf(urlParts[urlParts.length - 2]);
        return stackOverflowService.getAnswerInfoByQuestion(question);
    }

    public List<CommentInfo> getCommentsStackOverflowByUrl(String url) {
        String[] urlParts = url.split(SPLIT_URL);
        Long question = Long.valueOf(urlParts[urlParts.length - 2]);
        return stackOverflowService.getCommentInfoByQuestion(question);
    }

    public QuestionInfo getQuestionStackOverflowByUrl(String url) {
        String[] urlParts = url.split(SPLIT_URL);
        try {
            return stackOverflowService.getQuestionInfo(Long.valueOf(urlParts[urlParts.length - 2]));
        } catch (WebClientResponseException ex) {
            throw new InvalidUrlException(URL + url);
        }
    }
}
