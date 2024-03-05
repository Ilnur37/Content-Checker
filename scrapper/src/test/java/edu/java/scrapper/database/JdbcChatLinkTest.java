package edu.java.scrapper.database;

import edu.java.scrapper.dao.ChatLinkDao;
import edu.java.scrapper.model.chatLink.ChatLink;
import edu.java.scrapper.model.chatLink.ChatLinkRowMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JdbcChatLinkTest extends IntegrationTest {
    @Autowired
    private ChatLinkDao chatLinkDao;
    @Autowired
    public JdbcClient jdbcClient;
    @Autowired
    private ChatLinkRowMapper chatLinkRowMapper;
    private final String getAllSQL = "SELECT * FROM chat_link";
    private final String saveSQL = "INSERT INTO chat_link(chat_id, link_id) VALUES (%d, %d)";
    private final String defaultUrl = "defaultUrl";
    private final long defaultTgChatId = 10;

    private ChatLink createChatLink(long chatId, long linkId) {
        ChatLink chatLink = new ChatLink();
        chatLink.setChatId(chatId);
        chatLink.setLinkId(linkId);
        return chatLink;
    }

    private void insertRowIntoChat(long tgChatId) {
        jdbcClient.sql(String.format(
                "INSERT INTO chat(tg_chat_id, created_at) VALUES (%d, CURRENT_TIMESTAMP)",
                tgChatId
            ))
            .update();
    }

    private void insertRowIntoLink(String url) {
        jdbcClient.sql(String.format(
                "INSERT INTO link(url, created_at, last_update_at) VALUES ('%s', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                url
            ))
            .update();
    }

    private long getIdFromChat(long tgChatId) {
        return jdbcClient.sql("SELECT (id) FROM chat WHERE tg_chat_id = ?")
            .param(tgChatId)
            .query(Long.class).single();
    }

    private long getIdFromLink(String url) {
        return jdbcClient.sql("SELECT (id) FROM link WHERE url = ?")
            .param(url)
            .query(Long.class).single();
    }

    @BeforeEach
    public void checkThatTableIsEmpty() {
        List<ChatLink> content = jdbcClient.sql(getAllSQL)
            .query(chatLinkRowMapper).list();
        assertTrue(content.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("getByLinkId (В таблице chat_link 1 значение с искомой ссылкой)")
    void getByLinkIdWhenOneChat() {
        //Добавление в таблицы chat, link строк
        insertRowIntoChat(defaultTgChatId);
        insertRowIntoLink(defaultUrl);
        long chatId = getIdFromChat(defaultTgChatId);
        long linkId = getIdFromLink(defaultUrl);

        //Добавление связи в chat_link
        jdbcClient.sql(String.format(saveSQL, chatId, linkId))
            .update();

        List<ChatLink> contentByLinkId = chatLinkDao.getByLinkId(linkId);
        assertAll(
            () -> assertEquals(1, contentByLinkId.size()),
            () -> assertEquals(contentByLinkId.getFirst().getChatId(), chatId)
        );
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("getByLinkId (В таблице chat_link несколько значений с искомой ссылкой)")
    void getByLinkIdWhenManyChats() {
        int count = 5;
        long linkId;
        List<Long> chatIds = new ArrayList<>();
        //Добавление в таблицы chat, link строк
        insertRowIntoLink(defaultUrl);
        linkId = getIdFromLink(defaultUrl);
        for (int i = 0; i < count; i++) {
            insertRowIntoChat(defaultTgChatId + i);
            chatIds.add(getIdFromChat(defaultTgChatId + i));
        }

        //Добавление связи в chat_link
        for (long chatId : chatIds) {
            jdbcClient.sql(String.format(saveSQL, chatId, linkId))
                .update();
        }

        List<ChatLink> contentByLinkId = chatLinkDao.getByLinkId(linkId);
        assertEquals(chatIds.size(), contentByLinkId.size());
        for (int i = 0; i < chatIds.size(); i++) {
            assertEquals(chatIds.get(i), contentByLinkId.get(i).getChatId());
        }
    }

    @Test
    @Transactional
    @Rollback
    void getByChatId() {
        //Добавление в таблицы chat, link строк
        insertRowIntoChat(defaultTgChatId);
        insertRowIntoLink(defaultUrl);
        long chatId = getIdFromChat(defaultTgChatId);
        long linkId = getIdFromLink(defaultUrl);

        //Добавление связи в chat_link
        jdbcClient.sql(String.format(saveSQL, chatId, linkId))
            .update();

        List<ChatLink> contentByChatId = chatLinkDao.getByChatId(chatId);
        assertAll(
            () -> assertEquals(1, contentByChatId.size()),
            () -> assertEquals(contentByChatId.getFirst().getLinkId(), linkId)
        );
    }

    @Test
    @Transactional
    @Rollback
    void getAll() {
        int count = 5;
        long linkId;
        List<Long> chatIds = new ArrayList<>();

        //Добавление в таблицы chat, link строк
        insertRowIntoLink(defaultUrl);
        linkId = getIdFromLink(defaultUrl);
        for (int i = 0; i < count; i++) {
            insertRowIntoChat(defaultTgChatId + i);
            chatIds.add(getIdFromChat(defaultTgChatId + i));
        }

        //Добавление связи в chat_link
        for (long chatId : chatIds) {
            jdbcClient.sql(String.format(saveSQL, chatId, linkId))
                .update();
        }

        List<ChatLink> content = chatLinkDao.getAll();
        assertEquals(chatIds.size(), content.size());
        for (int i = 0; i < chatIds.size(); i++) {
            assertEquals(chatIds.get(i), content.get(i).getChatId());
            assertEquals(linkId, content.get(i).getLinkId());
        }
    }

    @Test
    @Transactional
    @Rollback
    void save() {
        //Добавление в таблицы chat, link строк
        insertRowIntoChat(defaultTgChatId);
        insertRowIntoLink(defaultUrl);
        long chatId = getIdFromChat(defaultTgChatId);
        long linkId = getIdFromLink(defaultUrl);

        //Добавление связи в chat_link
        chatLinkDao.save(createChatLink(chatId, linkId));

        List<ChatLink> content = jdbcClient.sql(getAllSQL)
            .query(chatLinkRowMapper).list();
        assertAll(
            "Поддтверждение, что появилась 1 связь",
            () -> assertFalse(content.isEmpty()),
            () -> assertEquals(content.getFirst().getLinkId(), linkId),
            () -> assertEquals(content.getFirst().getChatId(), chatId)
        );
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        //Добавление в таблицы chat, link строк
        insertRowIntoChat(defaultTgChatId);
        insertRowIntoLink(defaultUrl);
        long chatId = getIdFromChat(defaultTgChatId);
        long linkId = getIdFromLink(defaultUrl);

        //Добавление связи в chat_link
        jdbcClient.sql(String.format(saveSQL, chatId, linkId))
            .update();
        List<ChatLink> content = jdbcClient.sql(getAllSQL)
            .query(chatLinkRowMapper).list();
        assertAll(
            "Поддтверждение, что появилась 1 связь",
            () -> assertFalse(content.isEmpty()),
            () -> assertEquals(content.getFirst().getLinkId(), linkId),
            () -> assertEquals(content.getFirst().getChatId(), chatId)
        );

        //Удаление связи
        chatLinkDao.delete(createChatLink(chatId, linkId));
        List<ChatLink> actualContent = jdbcClient.sql(getAllSQL)
            .query(chatLinkRowMapper).list();
        assertTrue(actualContent.isEmpty());
    }
}
