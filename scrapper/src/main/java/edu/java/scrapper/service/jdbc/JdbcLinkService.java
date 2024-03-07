package edu.java.scrapper.service.jdbc;

import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.scrapper.dao.ChatDao;
import edu.java.scrapper.dao.ChatLinkDao;
import edu.java.scrapper.dao.LinkDao;
import edu.java.scrapper.exception.custom.LinkNotFoundException;
import edu.java.scrapper.exception.custom.ReAddLinkException;
import edu.java.scrapper.model.chat.Chat;
import edu.java.scrapper.model.chatLink.ChatLink;
import edu.java.scrapper.model.link.Link;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static java.time.OffsetDateTime.now;

@Service
@RequiredArgsConstructor
@Transactional
public class JdbcLinkService {
    private static final String EX_CHAT = "tg_chat_id = ";
    private static final String EX_LINK = "url = ";
    private final ChatDao chatDao;
    private final LinkDao linkDao;
    private final ChatLinkDao chatLinkDao;

    public ListLinksResponse getAll(long tgChatId) {
        long chatId = getChatId(tgChatId);
        List<ChatLink> chatLinksByChat = chatLinkDao.getByChatId(chatId);

        //Удаление строк из chat_link с отсутствующей записью в link
        List<Long> emptyLinksInChatLink = chatLinksByChat.stream()
            .map(ChatLink::getLinkId)
            .filter(linkId -> linkDao.getById(linkId).isEmpty())
            .toList();
        for (long linkId : emptyLinksInChatLink) {
            ChatLink chatLink = createChatLink(chatId, linkId);
            chatLinkDao.delete(chatLink);
        }

        List<LinkResponse> linkResponses = chatLinksByChat.stream()
            .map(row -> linkDao.getById(row.getLinkId()))
            .map(Optional::get)
            .map(link -> new LinkResponse(link.getId(), link.getUrl()))
            .toList();

        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    public LinkResponse add(long tgChatId, AddLinkRequest linkRequest) {
        String url = linkRequest.link();
        long chatId = getChatId(tgChatId);
        Link resultLink;

        if (linkDao.getByUrl(url).isEmpty()) {
            Link createLink = new Link();
            createLink.setUrl(url);
            createLink.setCreatedAt(now());
            createLink.setLastUpdateAt(now());
            linkDao.save(createLink);
            resultLink = linkDao.getByUrl(url).get();
        } else {
            resultLink = linkDao.getByUrl(url).get();
            for (ChatLink chatLink : chatLinkDao.getByChatId(tgChatId)) {
                if (chatLink.getLinkId() == resultLink.getId()) {
                    throw new ReAddLinkException(
                        getExMsg(EX_CHAT, String.valueOf(tgChatId))
                            + ", "
                            + getExMsg(EX_LINK, resultLink.getUrl())
                    );
                }
            }
        }

        ChatLink chatLink = createChatLink(chatId, resultLink.getId());
        chatLinkDao.save(chatLink);

        return new LinkResponse(resultLink.getId(), resultLink.getUrl());
    }

    public LinkResponse remove(long tgChatId, RemoveLinkRequest linkRequest) {
        String url = linkRequest.link();
        long chatId = getChatId(tgChatId);
        long linkId = getLinkId(url);

        Link resultLink = linkDao.getById(linkId).get();
        ChatLink chatLink = createChatLink(chatId, linkId);
        chatLinkDao.delete(chatLink);

        if (chatLinkDao.getByLinkId(linkId).isEmpty()) {
            linkDao.delete(resultLink);
        }

        return new LinkResponse(resultLink.getId(), resultLink.getUrl());
    }

    private ChatLink createChatLink(long chatId, long linkId) {
        ChatLink chatLink = new ChatLink();
        chatLink.setChatId(chatId);
        chatLink.setLinkId(linkId);
        return chatLink;
    }

    private long getChatId(long id) {
        Chat chat = chatDao.getByTgChatId(id)
            .orElseThrow(
                () -> new ChatIdNotFoundException(getExMsg(EX_CHAT, String.valueOf(id)))
            );
        return chat.getId();
    }

    private long getLinkId(String url) {
        Link link = linkDao.getByUrl(url)
            .orElseThrow(
                () -> new LinkNotFoundException(getExMsg(EX_LINK, url))
            );
        return link.getId();
    }

    private String getExMsg(String ex, String value) {
        return ex + value;
    }
}
