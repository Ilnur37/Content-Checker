package edu.java.scrapper.service.web;

import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.dto.github.ActionsInfo;
import edu.java.scrapper.dto.github.RepositoryInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubService {
    private final GithubClient gitHubClient;

    public RepositoryInfo getRepositoryInfo(String owner, String repo) {
        return gitHubClient.getRepositoryInfo(owner, repo);
    }

    public List<ActionsInfo> getActionsInfo(String owner, String repo) {
        return gitHubClient.getActionsInfo(owner, repo);
    }
}
