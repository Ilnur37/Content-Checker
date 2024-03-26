package edu.java.scrapper.database.service.jpa;

import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.ReAddLinkException;
import edu.java.scrapper.database.JpaIntegrationTest;
import edu.java.scrapper.domain.jpa.model.Chat;
import edu.java.scrapper.domain.jpa.model.Link;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Rollback
@Transactional
public class JpaLinkServiceTest extends JpaIntegrationTest {

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    @DisplayName("Получить все ссылки пользователя")
    void getAll() {
        ListLinksResponse response = linkService.getAll(defaultTgChatId);

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
            () -> linkService.getAll(defaultTgChatId + 1)
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @DisplayName("Добавить ссылку")
    void add() {
        String url = defaultUrl + defaultUrl;
        LinkResponse response = linkService.add(defaultTgChatId, new AddLinkRequest(url));

        Chat chat = chatRepository.findChatByTgChatId(defaultTgChatId).orElseThrow();
        assertAll(
            () -> assertEquals(url, response.url()),
            () -> assertEquals(1, chat.getLinks().size())
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @DisplayName("Добавить ссылку, неверный chatId")
    void addWhenChatIdNotFound() {
        assertThrows(
            ChatIdNotFoundException.class,
            () -> linkService.add(defaultTgChatId + 1, new AddLinkRequest(defaultUrl))
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
            () -> linkService.add(defaultTgChatId, new AddLinkRequest(defaultUrl))
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    @DisplayName("Удалить ссылку, отслеживаемую одним чатом")
    void removeWhenOneChatTrack() {
        linkService.remove(defaultTgChatId, new RemoveLinkRequest(defaultUrl));

        Chat chat = chatRepository.findChatByTgChatId(defaultTgChatId).orElseThrow();
        Optional<Link> actualLink = linkRepository.findLinkByUrl(defaultUrl);
        assertAll(
            "Ссылка удалена из всех таблиц",
            () -> assertEquals(0, chat.getLinks().size()),
            () -> assertTrue(actualLink.isPresent())
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(statements = "INSERT INTO chat(id, tg_chat_id, created_at) OVERRIDING SYSTEM VALUE\n" +
        "VALUES (100, 100, CURRENT_TIMESTAMP)\n" +
        "ON CONFLICT DO NOTHING;")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    @Sql(statements = "INSERT INTO chat_link(id, chat_id, link_id) OVERRIDING SYSTEM VALUE\n" +
        "VALUES (2, 100, 10)\n" +
        "ON CONFLICT DO NOTHING;")
    @DisplayName("Удалить ссылку, отслеживаемую несколькими чатами")
    void removeWhenManyChatsTrack() {
        linkService.remove(defaultTgChatId, new RemoveLinkRequest(defaultUrl));

        Chat chat1 = chatRepository.findChatByTgChatId(defaultTgChatId).orElseThrow();
        Chat chat2 = chatRepository.findChatByTgChatId(100).orElseThrow();
        Link actualLink = linkRepository.findLinkByUrl(defaultUrl).orElseThrow();

        assertAll(
            "Удалена только 1 запись о связи",
            () -> assertEquals(0, chat1.getLinks().size()),
            () -> assertEquals(1, chat2.getLinks().size()),
            () -> assertEquals(1, actualLink.getChats().size())
        );
    }
}
