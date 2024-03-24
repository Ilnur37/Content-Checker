package edu.java.scrapper.scheduler;

import edu.java.scrapper.domain.jdbc.dao.JdbcChatLinkDao;
import edu.java.scrapper.domain.jdbc.dao.JdbcLinkDao;
import edu.java.scrapper.domain.jdbc.model.link.Link;
import edu.java.scrapper.domain.model.ChatLinkWithTgChat;
import edu.java.scrapper.dto.github.ActionsInfo;
import edu.java.scrapper.dto.stackoverflow.answer.AnswerInfo;
import edu.java.scrapper.dto.stackoverflow.comment.CommentInfo;
import edu.java.scrapper.service.BotService;
import edu.java.scrapper.service.web.WebResourceHandler;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import static java.lang.String.format;

@ConditionalOnProperty(value = "app.scheduler.enable", havingValue = "true", matchIfMissing = true)
@Slf4j
@RequiredArgsConstructor
public class JdbcLinkUpdaterScheduler {
    private static final Duration NEED_TO_CHECK = Duration.ofSeconds(30);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm, dd.MM.yyyy");
    private static final String GIT_HEAD = "В репозитории %s, пользователя %s, %d новых изменений:\n";
    private static final String GIT_ABOUT = "В ветку %s были внесены изменения (тип: %s, время %s)\n";
    private static final String SOF_HEAD = "В вопросе %s, пользователя %s появились новые ответы/кометарии:\n";
    private static final String SOF_ANSWER = "Новый ответ от пользователя %s (время %s)\n";
    private static final String SOF_COMMENT = "Новый комментарий от пользователя %s (время %s)\n";
    private final JdbcLinkDao linkDao;
    private final JdbcChatLinkDao chatLinkDao;
    private final BotService botService;
    private final WebResourceHandler webResourceHandler;

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        linkDao.deleteUnnecessary();
        OffsetDateTime now = OffsetDateTime.now();
        List<Link> links = linkDao.getByLastCheck(now.minus(NEED_TO_CHECK));
        for (Link link : links) {
            if (webResourceHandler.isGitHubUrl(link.getUrl())) {
                gitHubProcess(link, now);
            } else {
                stackOverflowProcess(link, now);
            }
            linkDao.updateLastCheckAtById(link.getId(), now);
        }
    }

    private void gitHubProcess(Link link, OffsetDateTime now) {
        List<ActionsInfo> actionsInfo = webResourceHandler.getActionsGitHubInfoByUrl(link.getUrl());
        if (link.getLastUpdateAt().isBefore(actionsInfo.getFirst().getPushedAt())) {
            StringBuilder description =
                new StringBuilder(format(GIT_HEAD, link.getName(), link.getAuthor(), actionsInfo.size()));
            actionsInfo.stream()
                .filter(repI -> repI.getPushedAt().isAfter(link.getLastUpdateAt()))
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
        List<AnswerInfo> newAnswers = webResourceHandler.getAnswersStackOverflowByUrl(link.getUrl())
            .stream()
            .filter(answer -> answer.getLastActivityDate().isAfter(link.getLastUpdateAt()))
            .toList();
        List<CommentInfo> newComments = webResourceHandler.getCommentsStackOverflowByUrl(link.getUrl())
            .stream()
            .filter(comment -> comment.getCreationDate().isAfter(link.getLastUpdateAt()))
            .toList();

        if (!newAnswers.isEmpty() || !newComments.isEmpty()) {
            StringBuilder description =
                new StringBuilder(format(SOF_HEAD, link.getName(), link.getAuthor()));
            newAnswers.forEach(answer -> description.append(format(
                SOF_ANSWER,
                answer.getOwner().getDisplayName(),
                answer.getLastEditDate() == null ? answer.getLastActivityDate().format(FORMATTER)
                    : answer.getLastEditDate().format(FORMATTER)
            )));
            newComments.forEach(comment -> description.append(format(
                SOF_COMMENT,
                comment.getOwner().getDisplayName(),
                comment.getCreationDate().format(FORMATTER)
            )));
            updateTablesAndSendMsg(link, now, description.toString());
        }
    }

    private void updateTablesAndSendMsg(Link link, OffsetDateTime newUpdateTime, String description) {
        long linkId = link.getId();
        linkDao.updateLastUpdateAtById(linkId, newUpdateTime);
        List<Long> chatIdsToSendMsg = chatLinkDao.getByLinkIdJoinChat(linkId)
            .stream()
            .map(ChatLinkWithTgChat::getTgChatId)
            .toList();
        botService.sendUpdate(linkId, link.getUrl(), description, chatIdsToSendMsg);
    }
}
