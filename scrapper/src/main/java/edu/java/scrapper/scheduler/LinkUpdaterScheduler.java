package edu.java.scrapper.scheduler;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.domain.jdbc.dao.ChatLinkDao;
import edu.java.scrapper.domain.jdbc.dao.LinkDao;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLinkWithTgChat;
import edu.java.scrapper.domain.jdbc.model.link.Link;
import edu.java.scrapper.dto.github.RepositoryInfo;
import edu.java.scrapper.dto.stackoverflow.question.QuestionInfo;
import edu.java.scrapper.service.BotService;
import edu.java.scrapper.service.web.GitHubService;
import edu.java.scrapper.service.web.StackOverflowService;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "app.scheduler.enable", havingValue = "true", matchIfMissing = true)
@Slf4j
public class LinkUpdaterScheduler {
    private final LinkDao linkDao;
    private final ChatLinkDao chatLinkDao;
    private final BotService botService;
    private final GitHubService gitHubService;
    private final StackOverflowService stackOverflowService;
    private final ApplicationConfig.Supported api;
    private static final Duration NEED_TO_CHECK = Duration.ofSeconds(30);

    public LinkUpdaterScheduler(
        LinkDao linkDao,
        ChatLinkDao chatLinkDao,
        BotService botService,
        GitHubService gitHubService,
        StackOverflowService stackOverflowService,
        ApplicationConfig appConf
    ) {
        this.linkDao = linkDao;
        this.chatLinkDao = chatLinkDao;
        this.botService = botService;
        this.gitHubService = gitHubService;
        this.stackOverflowService = stackOverflowService;
        this.api = appConf.supported();
    }

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        OffsetDateTime now = OffsetDateTime.now();
        List<Link> links = linkDao.getByLastUpdate(now.minus(NEED_TO_CHECK));
        for (Link link : links) {
            if (link.getUrl().contains(api.github())) {
                gitHubProcess(link);
            } else {
                stackOverflowProcess(link);
            }
        }
    }

    private void gitHubProcess(Link link) {
        String[] url = link.getUrl().split("/");
        String owner = url[url.length - 2];
        String repo = url[url.length - 1];
        RepositoryInfo repositoryInfo = gitHubService.getRepositoryInfo(owner, repo);
        if (link.getLastUpdateAt().isBefore(repositoryInfo.getPushedAt())) {
            updateTablesAndSendMsg(link.getId(), repositoryInfo.getPushedAt(), link.getUrl());
        }
    }

    private void stackOverflowProcess(Link link) {
        String[] url = link.getUrl().split("/");
        Long question = Long.valueOf(url[url.length - 2]);
        QuestionInfo questionInfo = stackOverflowService.getQuestionInfo(question);
        if (link.getLastUpdateAt().isBefore(questionInfo.getLastActivityDate())) {
            updateTablesAndSendMsg(link.getId(), questionInfo.getLastActivityDate(), link.getUrl());
        }
    }

    private void updateTablesAndSendMsg(long linkId, OffsetDateTime newUpdateTime, String url) {
        linkDao.updateLastUpdateAtById(linkId, newUpdateTime);
        List<Long> chatIdsToSendMsg = chatLinkDao.getByLinkIdIdJoinChat(linkId)
            .stream()
            .map(ChatLinkWithTgChat::getTgChatId)
            .toList();
        botService.sendUpdate(linkId, url, "template description", chatIdsToSendMsg);
    }
}
