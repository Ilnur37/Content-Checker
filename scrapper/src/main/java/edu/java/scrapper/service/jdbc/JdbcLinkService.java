package edu.java.scrapper.service.jdbc;

import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.LinkNotFoundException;
import edu.java.models.exception.ReAddLinkException;
import edu.java.scrapper.dao.ChatDao;
import edu.java.scrapper.dao.ChatLinkDao;
import edu.java.scrapper.dao.LinkDao;
import edu.java.scrapper.dto.github.RepositoryInfo;
import edu.java.scrapper.dto.stackoverflow.question.QuestionInfo;
import edu.java.scrapper.model.chat.Chat;
import edu.java.scrapper.model.chatLink.ChatLink;
import edu.java.scrapper.model.chatLink.ChatLinkWithUrl;
import edu.java.scrapper.model.link.Link;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.web.WebResourceHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static java.time.OffsetDateTime.now;

@Service
@RequiredArgsConstructor
@Transactional
public class JdbcLinkService implements LinkService {
    private static final String EMPTY_STRING = "";
    private final ChatDao chatDao;
    private final LinkDao linkDao;
    private final ChatLinkDao chatLinkDao;
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

        long chatId = getChatByTgChatId(tgChatId).getId();
        Link actualLink;

        //Создание ссылки в таблице ссылок, если ее нет
        if (linkDao.getByUrl(url).isEmpty()) {
            Link createLink = new Link();
            createLink.setUrl(url);
            createLink.setCreatedAt(now());
            createLink.setLastUpdateAt(now());
            createLink.setAuthor(author);
            createLink.setName(title);
            linkDao.save(createLink);
            actualLink = linkDao.getByUrl(url).get();
        } else {
            //Иначе проверка на предмет повторного добавления
            actualLink = linkDao.getByUrl(url).get();
            for (ChatLink chatLink : chatLinkDao.getByChatId(chatId)) {
                if (chatLink.getLinkId() == actualLink.getId()) {
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

    private Chat getChatByTgChatId(long id) {
        return chatDao.getByTgChatId(id)
            .orElseThrow(
                () -> new ChatIdNotFoundException(toExMsg(EX_CHAT, String.valueOf(id)))
            );
    }

    private Link getLinkByUrl(String url) {
        return linkDao.getByUrl(url)
            .orElseThrow(
                () -> new LinkNotFoundException(toExMsg(EX_LINK, url))
            );
    }
}
