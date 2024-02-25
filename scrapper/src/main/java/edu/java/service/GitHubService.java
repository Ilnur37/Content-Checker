package edu.java.service;

import edu.java.client.GithubClient;
import edu.java.dto.github.RepositoryInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubService {
    private final GithubClient gitHubClient;

    public RepositoryInfo getRepositoryInfo(String owner, String repo) {
        return gitHubClient.getRepositoryInfo(owner, repo);
    }
}
