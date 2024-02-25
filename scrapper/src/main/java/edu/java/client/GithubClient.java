package edu.java.client;

import edu.java.dto.github.RepositoryInfo;
import org.springframework.web.reactive.function.client.WebClient;

public class GithubClient extends Client {

    public GithubClient(WebClient webClient) {
        super(webClient);
    }

    public static GithubClient create(String baseUrl) {
        WebClient webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();

        return new GithubClient(webClient);
    }

    public RepositoryInfo getRepositoryInfo(String owner, String repo) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}", owner, repo)
            .retrieve()
            .bodyToMono(RepositoryInfo.class)
            .block();
    }
}
