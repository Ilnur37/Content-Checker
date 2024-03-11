package edu.java.scrapper.scheduler;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dao.ChatLinkDao;
import edu.java.scrapper.dao.LinkDao;
import edu.java.scrapper.dto.github.RepositoryInfo;
import edu.java.scrapper.dto.stackoverflow.answer.AnswerInfo;
import edu.java.scrapper.dto.stackoverflow.comment.CommentInfo;
import edu.java.scrapper.model.chatLink.ChatLinkWithTgChat;
import edu.java.scrapper.model.link.Link;
import edu.java.scrapper.service.BotService;
import edu.java.scrapper.service.web.GitHubService;
import edu.java.scrapper.service.web.StackOverflowService;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import static java.lang.String.format;

@Component
@ConditionalOnProperty(value = "app.scheduler.enable", havingValue = "true", matchIfMissing = true)
@Slf4j
public class LinkUpdaterScheduler {
    private static final Duration NEED_TO_CHECK = Duration.ofSeconds(30);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm, dd.MM.yyyy");
    private static final String GIT_HEAD = "В репозитории %s, пользователя %s, %d новых изменений:\n";
    private static final String GIT_ABOUT = "В ветку %s были внесены изменения (тип: %s, время %s)\n";
    private static final String SOF_HEAD = "В вопросе %s, пользователя %s появились новые ответы/коометарии:\n";
    private static final String SOF_ANSWER = "Новый ответ от польльзователя %s (время %s)\n";
    private static final String SOF_COMMENT = "Новый комментарий от польльзователя %s (время %s)\n";
    private final LinkDao linkDao;
    private final ChatLinkDao chatLinkDao;
    private final BotService botService;
    private final GitHubService gitHubService;
    private final StackOverflowService stackOverflowService;
    private final ApplicationConfig.Supported api;

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
        List<Link> links = linkDao.getByLustUpdate(now.minus(NEED_TO_CHECK));
        for (Link link : links) {
            if (link.getUrl().contains(api.github())) {
                gitHubProcess(link, now);
            } else {
                stackOverflowProcess(link, now);
            }
        }
    }

    private void gitHubProcess(Link link, OffsetDateTime now) {
        String[] url = link.getUrl().split("/");
        String owner = url[url.length - 2];
        String repo = url[url.length - 1];
        List<RepositoryInfo> repositoryInfo = gitHubService.getRepositoryInfo(owner, repo);
        if (link.getLastUpdateAt().isBefore(repositoryInfo.getFirst().getPushedAt())) {
            StringBuilder description =
                new StringBuilder(format(GIT_HEAD, repo, owner, repositoryInfo.size()));
            repositoryInfo.stream()
                .filter(repI -> repI.getPushedAt().isBefore(now))
                .map(repI -> format(
                    GIT_ABOUT,
                    repI.getRef(),
                    repI.getActivityType(),
                    repI.getPushedAt().format(FORMATTER)
                ))
                .forEach(description::append);
            updateTablesAndSendMsg(link, now, description.toString());
        }
    }

    private void stackOverflowProcess(Link link, OffsetDateTime now) {
        String[] url = link.getUrl().split("/");
        Long question = Long.valueOf(url[url.length - 2]);
        List<AnswerInfo> newAnswers = stackOverflowService.getAnswerInfoByQuestion(question)
            .stream()
            .filter(answer -> answer.getLastActivityDate().isBefore(now))
            .toList();
        List<CommentInfo> newComments = stackOverflowService.getCommentInfoByQuestion(question)
            .stream()
            .filter(comment -> comment.getCreationDate().isBefore(now))
            .toList();

        if (!newAnswers.isEmpty() || !newComments.isEmpty()) {
            StringBuilder description =
                new StringBuilder(format(SOF_HEAD, link.getName(), link.getAuthor()));
            newAnswers.forEach(answer -> description.append(format(
                SOF_ANSWER,
                answer.getOwner().getDisplayName(),
                answer.getLastEditDate()
            )));
            newComments.forEach(comment -> description.append(format(
                SOF_COMMENT,
                comment.getOwner().getDisplayName(),
                comment.getCreationDate()
            )));
            updateTablesAndSendMsg(link, now, description.toString());
        }
    }

    private void updateTablesAndSendMsg(Link link, OffsetDateTime newUpdateTime, String description) {
        long linkId = link.getId();
        linkDao.updateLastUpdateAtById(linkId, newUpdateTime);
        List<Long> chatIdsToSendMsg = chatLinkDao.getByLinkIdIdJoinChat(linkId)
            .stream()
            .map(ChatLinkWithTgChat::getTgChatId)
            .toList();
        botService.sendUpdate(linkId, link.getUrl(), description, chatIdsToSendMsg);
    }
}
