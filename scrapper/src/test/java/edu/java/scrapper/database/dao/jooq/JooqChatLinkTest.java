package edu.java.scrapper.database.dao.jooq;

import edu.java.scrapper.database.JooqIntegrationTest;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.ChatLink;
import edu.java.scrapper.domain.model.ChatLinkWithTgChat;
import edu.java.scrapper.domain.model.ChatLinkWithUrl;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@Rollback
public class JooqChatLinkTest extends JooqIntegrationTest {

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    @DisplayName("getByLinkId (В таблице chat_link 1 значение с искомой ссылкой)")
    void getByLinkIdWhenOneChat() {
        List<ChatLink> contentByLinkId = chatLinkDao.getByLinkId(defaultId);

        assertAll(
            () -> assertEquals(1, contentByLinkId.size()),
            () -> assertEquals(defaultId, contentByLinkId.getFirst().getChatId())
        );
    }

    @Test
    @Sql(value = "/sql/insertFiveRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertFiveRowChatLink.sql")
    @DisplayName("getByLinkId (В таблице chat_link несколько значений с искомой ссылкой)")
    void getByLinkIdWhenManyChats() {
        int count = 5;

        List<ChatLink> contentByLinkId = chatLinkDao.getByLinkId(defaultId);
        assertEquals(count, contentByLinkId.size());
    }

    @Test
    @Sql(value = "/sql/insertFiveRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertFiveRowChatLink.sql")
    @DisplayName("getByLinkIdIdJoinChat (В таблице chat_link несколько значений с искомой ссылкой)")
    void getByLinkIdIdJoinChatWhenManyChats() {
        int count = 5;

        List<ChatLinkWithTgChat> contentByLinkId = chatLinkDao.getByLinkIdJoinChat(defaultId);

        assertEquals(count, contentByLinkId.size());
        for (int i = 0; i < count; i++) {
            assertEquals(i + 1, contentByLinkId.get(i).getChatId());
            assertEquals(i + 1, contentByLinkId.get(i).getTgChatId());
        }
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    void getByChatId() {
        List<ChatLink> contentByChatId = chatLinkDao.getByChatId(defaultId);

        assertAll(
            () -> assertEquals(1, contentByChatId.size()),
            () -> assertEquals(defaultId, contentByChatId.getFirst().getLinkId())
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    void getByChatIdJoinLink() {
        List<ChatLinkWithUrl> contentByChatId = chatLinkDao.getByChatIdJoinLink(defaultId);
        assertAll(
            () -> assertEquals(1, contentByChatId.size()),
            () -> assertEquals(defaultId, contentByChatId.getFirst().getLinkId()),
            () -> assertEquals(defaultUrl, contentByChatId.getFirst().getUrl())
        );
    }

    @Test
    @Sql(value = "/sql/insertFiveRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertFiveRowChatLink.sql")
    void getAll() {
        int count = 5;
        List<ChatLink> content = chatLinkDao.getAll();

        assertEquals(count, content.size());
        for (int i = 0; i < count; i++) {
            assertEquals(i + 1, content.get(i).getChatId());
            assertEquals(defaultId, content.get(i).getLinkId());
        }
    }

    @Test
    @Sql(value = "/sql/insertFiveRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    void save() {
        //Добавление связи в chat_link
        ChatLink chatLink = new ChatLink();
        chatLink.setChatId(defaultId);
        chatLink.setLinkId(defaultId);
        chatLinkDao.save(chatLink);

        List<ChatLink> content = chatLinkDao.getAll();

        assertAll(
            "Подтверждение, что появилась 1 связь",
            () -> assertFalse(content.isEmpty()),
            () -> assertEquals(defaultId, content.getFirst().getLinkId()),
            () -> assertEquals(defaultId, content.getFirst().getChatId())
        );
    }

    @Test
    @Sql(value = "/sql/insertFiveRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    void remove() {
        //Удаление связи
        chatLinkDao.delete(defaultId, defaultId);

        List<ChatLink> actualContent = chatLinkDao.getAll();
        assertTrue(actualContent.isEmpty());
    }
}
