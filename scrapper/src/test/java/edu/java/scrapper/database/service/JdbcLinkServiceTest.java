package edu.java.scrapper.database.service;

import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.ReAddLinkException;
import edu.java.scrapper.database.IntegrationTest;
import edu.java.scrapper.domain.jdbc.dao.ChatDao;
import edu.java.scrapper.domain.jdbc.dao.ChatLinkDao;
import edu.java.scrapper.domain.jdbc.dao.LinkDao;
import edu.java.scrapper.domain.jdbc.model.chat.Chat;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLink;
import edu.java.scrapper.domain.jdbc.model.link.Link;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
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
    private final long tgChatId = 1;
    private final long defaultId = 1;
    private final String defaultUrl = "defaultUrl";
    private final OffsetDateTime time = OffsetDateTime.now();

    private Chat createChat() {
        return Chat.createChat(tgChatId, time);
    }

    private Link createLink() {
        return Link.createLink(defaultUrl, time, time);
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    @DisplayName("Получить все ссылки пользователя")
    void getAll() {
        ListLinksResponse response = linkService.getAll(tgChatId);
        assertEquals(1, response.size());
        assertEquals(defaultUrl, response.links().getFirst().url());
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    @DisplayName("Получить все ссылки пользователя, неверный chatId")
    void getAllWhenChatIdNotFound() {
        assertThrows(
            ChatIdNotFoundException.class,
            () -> linkService.getAll(tgChatId + 1)
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @DisplayName("Добавить ссылку")
    void add() {
        LinkResponse response = linkService.add(tgChatId, new AddLinkRequest(defaultUrl));

        ChatLink actualChatLink = chatLinkDao.getByChatId(defaultId).getFirst();
        Link actualLink = linkDao.findByUrl(defaultUrl).orElseThrow();
        assertAll(
            () -> assertEquals(defaultUrl, response.url()),
            () -> assertEquals(defaultUrl, actualLink.getUrl()),
            () -> assertEquals(actualLink.getId(), actualChatLink.getLinkId()),
            () -> assertEquals(defaultId, actualChatLink.getChatId())
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @DisplayName("Добавить ссылку, неверный chatId")
    void addWhenChatIdNotFound() {
        assertThrows(
            ChatIdNotFoundException.class,
            () -> linkService.add(tgChatId + 1, new AddLinkRequest(defaultUrl))
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    @DisplayName("Добавить ссылку, повторное добавление")
    void addWhenReAddLink() {
        assertThrows(
            ReAddLinkException.class,
            () -> linkService.add(tgChatId, new AddLinkRequest(defaultUrl))
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    @DisplayName("Удалить ссылку, отслеживаемую одним чатом (каскадное удаление)")
    void removeWhenOneChatTrack() {
        linkService.remove(tgChatId, new RemoveLinkRequest(defaultUrl));

        List<ChatLink> actualChatLink = chatLinkDao.getByChatId(defaultId);
        Optional<Link> actualLink = linkDao.findByUrl(defaultUrl);
        assertAll(
            "Ссылка удалена из всех таблиц",
            () -> assertEquals(0, actualChatLink.size()),
            () -> assertTrue(actualLink.isEmpty())
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(statements = "INSERT INTO chat(id, tg_chat_id, created_at) OVERRIDING SYSTEM VALUE\n" +
        "VALUES (10, 10, CURRENT_TIMESTAMP)\n" +
        "ON CONFLICT DO NOTHING;")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    @Sql(statements = "INSERT INTO chat_link(chat_id, link_id) OVERRIDING SYSTEM VALUE\n" +
        "VALUES (10, 1)\n" +
        "ON CONFLICT DO NOTHING;")
    @DisplayName("Удалить ссылку, отслеживаемую несколькими чатами")
    void removeWhenManyChatsTrack() {
        linkService.remove(tgChatId, new RemoveLinkRequest(defaultUrl));

        List<ChatLink> actualChatLink = chatLinkDao.getAll();
        Optional<Link> actualLink = linkDao.findByUrl(defaultUrl);
        assertAll(
            "Удалена только 1 запись о связи",
            () -> assertEquals(1, actualChatLink.size()),
            () -> assertTrue(actualLink.isPresent())
        );
    }
}
