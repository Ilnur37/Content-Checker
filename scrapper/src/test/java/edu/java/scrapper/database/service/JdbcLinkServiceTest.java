package edu.java.scrapper.database.service;

import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.scrapper.dao.ChatDao;
import edu.java.scrapper.dao.ChatLinkDao;
import edu.java.scrapper.dao.LinkDao;
import edu.java.scrapper.database.IntegrationTest;
import edu.java.models.exception.ReAddLinkException;
import edu.java.scrapper.model.chatLink.ChatLink;
import edu.java.scrapper.model.link.Link;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.database.UtilityDb.createChat;
import static edu.java.scrapper.database.UtilityDb.createChatLink;
import static edu.java.scrapper.database.UtilityDb.createLink;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Rollback
@Transactional
public class JdbcLinkServiceTest extends IntegrationTest {
    @Autowired
    JdbcLinkService linkService;
    @Autowired
    private ChatDao chatDao;
    @Autowired
    private LinkDao linkDao;
    @Autowired
    private ChatLinkDao chatLinkDao;
    private final long tgChatId = 10;
    private final String url = "url";

    @Test
    @DisplayName("Получить все ссылки пользователя")
    void getAll() {
        //Заполнение таблиц
        short countLinks = 8;
        chatDao.save(createChat(tgChatId));
        long chatId = chatDao.getByTgChatId(tgChatId).orElseThrow().getId();
        for (int i = 0; i < countLinks; i++) {
            String tempUrl = url + i;
            linkDao.save(createLink(tempUrl));
            long linkId = linkDao.getByUrl(tempUrl).orElseThrow().getId();
            chatLinkDao.save(createChatLink(chatId, linkId));
        }
        ListLinksResponse response = linkService.getAll(tgChatId);
        assertEquals(response.size(), countLinks);
        for (int i = 0; i < countLinks; i++) {
            assertEquals(url + i, response.links().get(i).url());
        }
    }

    @Test
    @DisplayName("Получить все ссылки пользователя, неверный chatId")
    void getAllWhenChatIdNotFound() {
        //Заполнение таблиц
        chatDao.save(createChat(tgChatId));
        long chatId = chatDao.getByTgChatId(tgChatId).orElseThrow().getId();
        linkDao.save(createLink(url));
        long linkId = linkDao.getByUrl(url).orElseThrow().getId();
        chatLinkDao.save(createChatLink(chatId, linkId));

        assertThrows(
            ChatIdNotFoundException.class,
            () -> linkService.getAll(tgChatId + 1)
        );
    }

    @Test
    @DisplayName("Добавить ссылку")
    void add() {
        //Заполнение таблиц
        chatDao.save(createChat(tgChatId));
        long chatId = chatDao.getByTgChatId(tgChatId).orElseThrow().getId();
        LinkResponse response = linkService.add(tgChatId, new AddLinkRequest(url));

        ChatLink actualChatLink = chatLinkDao.getByChatId(chatId).getFirst();
        Link actualLink = linkDao.getByUrl(url).orElseThrow();

        assertAll(
            () -> assertEquals(url, response.url()),
            () -> assertEquals(url, actualLink.getUrl()),
            () -> assertEquals(actualLink.getId(), actualChatLink.getLinkId()),
            () -> assertEquals(chatId, actualChatLink.getChatId())
        );
    }

    @Test
    @DisplayName("Добавить ссылку, неверный chatId")
    void addWhenChatIdNotFound() {
        //Заполнение таблиц
        chatDao.save(createChat(tgChatId));

        assertThrows(
            ChatIdNotFoundException.class,
            () -> linkService.add(tgChatId + 1, new AddLinkRequest(url))
        );
    }

    @Test
    @DisplayName("Добавить ссылку, повторное добавление")
    void addWhenReAddLink() {
        //Заполнение таблиц
        chatDao.save(createChat(tgChatId));
        linkService.add(tgChatId, new AddLinkRequest(url));

        assertThrows(
            ReAddLinkException.class,
            () -> linkService.add(tgChatId, new AddLinkRequest(url))
        );
    }

    @Test
    @DisplayName("Удалить ссылку, отслеживаемую одним чатом (каскадное удаление)")
    void removeWhenOneChatTrack() {
        //Заполнение таблиц
        chatDao.save(createChat(tgChatId));
        long chatId = chatDao.getByTgChatId(tgChatId).orElseThrow().getId();
        linkDao.save(createLink(url));
        long linkId = linkDao.getByUrl(url).orElseThrow().getId();
        chatLinkDao.save(createChatLink(chatId, linkId));

        ChatLink tempChatLink = chatLinkDao.getByChatId(chatId).getFirst();
        Link tempLink = linkDao.getByUrl(url).orElseThrow();
        assertAll(
            "Проверка добавления ссылки",
            () -> assertEquals(url, tempLink.getUrl()),
            () -> assertEquals(linkId, tempLink.getId()),
            () -> assertEquals(linkId, tempChatLink.getLinkId()),
            () -> assertEquals(chatId, tempChatLink.getChatId())
        );

        linkService.remove(tgChatId, new RemoveLinkRequest(url));

        List<ChatLink> actualChatLink = chatLinkDao.getByChatId(chatId);
        Optional<Link> actualLink = linkDao.getByUrl(url);
        assertAll(
            "Ссылка удалена из всех таблиц",
            () -> assertEquals(0, actualChatLink.size()),
            () -> assertTrue(actualLink.isEmpty())
        );
    }

    @Test
    @DisplayName("Удалить ссылку, отслеживаемую несколькими чатами")
    void removeWhenManyChatsTrack() {
        //Заполнение таблиц
        chatDao.save(createChat(tgChatId));
        long chatId1 = chatDao.getByTgChatId(tgChatId).orElseThrow().getId();
        chatDao.save(createChat(tgChatId + 1));
        long chatId2 = chatDao.getByTgChatId(tgChatId + 1).orElseThrow().getId();
        linkDao.save(createLink(url));
        long linkId = linkDao.getByUrl(url).orElseThrow().getId();
        chatLinkDao.save(createChatLink(chatId1, linkId));
        chatLinkDao.save(createChatLink(chatId2, linkId));

        ChatLink tempChatLink1 = chatLinkDao.getByChatId(chatId1).getFirst();
        ChatLink tempChatLink2 = chatLinkDao.getByChatId(chatId2).getFirst();
        Link tempLink = linkDao.getByUrl(url).orElseThrow();
        assertAll(
            "Проверка добавления ссылки",
            () -> assertEquals(url, tempLink.getUrl()),
            () -> assertEquals(linkId, tempLink.getId()),
            () -> assertEquals(linkId, tempChatLink1.getLinkId()),
            () -> assertEquals(chatId1, tempChatLink1.getChatId()),
            () -> assertEquals(linkId, tempChatLink2.getLinkId()),
            () -> assertEquals(chatId2, tempChatLink2.getChatId())
        );

        linkService.remove(tgChatId, new RemoveLinkRequest(url));

        List<ChatLink> actualChatLink = chatLinkDao.getAll();
        Optional<Link> actualLink = linkDao.getByUrl(url);
        assertAll(
            "Удалена только 1 запись о связи",
            () -> assertEquals(1, actualChatLink.size()),
            () -> assertTrue(actualLink.isPresent()),
            () -> assertEquals(url, actualLink.get().getUrl())
        );
    }
}
