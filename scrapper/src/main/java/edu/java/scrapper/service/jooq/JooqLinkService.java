package edu.java.scrapper.service.jooq;

import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.LinkNotFoundException;
import edu.java.models.exception.ReAddLinkException;
import edu.java.scrapper.domain.jooq.dao.JooqChatDao;
import edu.java.scrapper.domain.jooq.dao.JooqChatLinkDao;
import edu.java.scrapper.domain.jooq.dao.JooqLinkDao;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.Chat;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.ChatLink;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.Link;
import edu.java.scrapper.domain.model.ChatLinkWithUrl;
import edu.java.scrapper.dto.github.RepositoryInfo;
import edu.java.scrapper.dto.stackoverflow.question.QuestionInfo;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.web.WebResourceHandler;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class JooqLinkService implements LinkService {
    private static final String EMPTY_STRING = "";

    private final JooqChatDao chatDao;
    private final JooqLinkDao linkDao;
    private final JooqChatLinkDao chatLinkDao;
    private final WebResourceHandler webResourceHandler;

    @Override
    public ListLinksResponse getAll(long tgChatId) {
        long chatId = getChatByTgChatId(tgChatId).getId();

        List<ChatLinkWithUrl> chatLinksByChat = chatLinkDao.getByChatIdJoinLink(chatId);
        List<LinkResponse> linkResponses = chatLinksByChat.stream()
            .map(row -> new LinkResponse(row.getLinkId(), row.getUrl()))
            .toList();

        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @Override
    public LinkResponse add(long tgChatId, AddLinkRequest linkRequest) {
        String url = linkRequest.link();
        long chatId = getChatByTgChatId(tgChatId).getId();
        String author = EMPTY_STRING;
        String title = EMPTY_STRING;
        if (webResourceHandler.isGitHubUrl(url)) {
            RepositoryInfo repositoryInfo = webResourceHandler.getRepositoryGitHubInfoByUrl(url);
            author = repositoryInfo.getActor().getLogin();
            title = repositoryInfo.getName();
        } else if (webResourceHandler.isStackOverflowUrl(url)) {
            QuestionInfo questionInfo = webResourceHandler.getQuestionStackOverflowByUrl(url);
            author = questionInfo.getOwner().getDisplayName();
            title = questionInfo.getTitle();
        }

        Link actualLink;
        //Создание ссылки в таблице ссылок, если ее нет
        if (linkDao.findByUrl(url).isEmpty()) {
            OffsetDateTime nowTime = OffsetDateTime.now();
            Link createLink = createLink(url, nowTime, nowTime, author, title, nowTime);
            linkDao.save(createLink);
            actualLink = linkDao.findByUrl(url).get();
        } else {
            //Иначе проверка на предмет повторного добавления
            actualLink = linkDao.findByUrl(url).get();
            for (ChatLinkWithUrl chatLink : chatLinkDao.getByChatIdJoinLink(chatId)) {
                if (Objects.equals(chatLink.getUrl(), url)) {
                    throw new ReAddLinkException(
                        toExMsg(EX_CHAT, String.valueOf(tgChatId))
                            + ", "
                            + toExMsg(EX_LINK, actualLink.getUrl())
                    );
                }
            }
        }

        ChatLink chatLink = createChatLink(chatId, actualLink.getId());
        chatLinkDao.save(chatLink);

        return new LinkResponse(actualLink.getId(), actualLink.getUrl());
    }

    @Override
    public LinkResponse remove(long tgChatId, RemoveLinkRequest linkRequest) {
        String url = linkRequest.link();
        Link actualLink = getLinkByUrl(url);
        long chatId = getChatByTgChatId(tgChatId).getId();
        long linkId = actualLink.getId();
        int countChatTrackLink = chatLinkDao.getByLinkId(linkId).size();
        chatLinkDao.delete(chatId, linkId);
        //Если ссылку отслеживает 1 чат, удалить из таблицы ссылок
        if (countChatTrackLink == 1) {
            linkDao.deleteByUrl(url);
        }

        return new LinkResponse(actualLink.getId(), actualLink.getUrl());
    }

    private ChatLink createChatLink(long chatId, long linkId) {
        ChatLink chatLink = new ChatLink();
        chatLink.setChatId(chatId);
        chatLink.setLinkId(linkId);
        return chatLink;
    }

    private Link createLink(
        String url,
        OffsetDateTime createdAt,
        OffsetDateTime lastUpdateAt,
        String name,
        String author,
        OffsetDateTime lastCheckAt
    ) {
        Link link = new Link();
        link.setUrl(url);
        link.setCreatedAt(createdAt);
        link.setLastUpdateAt(lastUpdateAt);
        link.setName(name);
        link.setAuthor(author);
        link.setLastCheckAt(lastCheckAt);
        return link;
    }

    private Chat getChatByTgChatId(long id) {
        return chatDao.findByTgChatId(id)
            .orElseThrow(
                () -> new ChatIdNotFoundException(toExMsg(EX_CHAT, String.valueOf(id)))
            );
    }

    private Link getLinkByUrl(String url) {
        return linkDao.findByUrl(url)
            .orElseThrow(
                () -> new LinkNotFoundException(toExMsg(EX_LINK, url))
            );
    }
}
