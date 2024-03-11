package edu.java.scrapper.service.web;

import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.dto.github.RepositoryInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubService {
    private final GithubClient gitHubClient;

    public List<RepositoryInfo> getRepositoryInfo(String owner, String repo) {
        return gitHubClient.getRepositoryInfo(owner, repo);
    }
}
