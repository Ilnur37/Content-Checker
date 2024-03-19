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

    //doc https://docs.github.com/ru/rest/repos/repos?apiVersion=2022-11-28#get-a-repository
    public RepositoryInfo getRepositoryInfo(String owner, String repo) {
        return gitHubClient.getRepositoryInfo(owner, repo);
    }

    //doc https://docs.github.com/ru/rest/repos/repos?apiVersion=2022-11-28#list-repository-activities
    public List<ActionsInfo> getActionsInfo(String owner, String repo) {
        return gitHubClient.getActionsInfo(owner, repo);
    }
}
