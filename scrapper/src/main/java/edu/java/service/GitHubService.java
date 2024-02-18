package edu.java.service;

import edu.java.dto.github.RepositoryInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GitHubService {
    private final WebClient gitHubClient;

    public GitHubService(WebClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public RepositoryInfo getRepositoryInfo(String owner, String repo) {
        return gitHubClient.get()
            .uri("/repos/{owner}/{repo}", owner, repo)
            .retrieve()
            .bodyToMono(RepositoryInfo.class)
            .block();
    }
}
