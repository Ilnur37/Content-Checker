package edu.java.scrapper.client;

import edu.java.scrapper.dto.github.ActionsInfo;
import edu.java.scrapper.dto.github.RepositoryInfo;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;

public class GithubClient extends Client {
    private static final String GET_REPOSITORY_INFO_URL = "/repos/{owner}/{repo}";
    private static final String GET_ACTIONS_INFO_URL = "/repos/{owner}/{repo}/activity";

    public GithubClient(WebClient webClient) {
        super(webClient);
    }

    public static GithubClient create(String baseUrl) {
        WebClient webClient = WebClient.create(baseUrl);
        return new GithubClient(webClient);
    }

    public RepositoryInfo getRepositoryInfo(String owner, String repo) {
        return webClient.get()
            .uri(GET_REPOSITORY_INFO_URL, owner, repo)
            .retrieve()
            .bodyToMono(RepositoryInfo.class)
            .block();
    }

    public List<ActionsInfo> getActionsInfo(String owner, String repo) {
        return webClient.get()
            .uri(GET_ACTIONS_INFO_URL, owner, repo)
            .retrieve()
            .bodyToFlux(ActionsInfo.class)
            .collectList()
            .block();
    }
}
