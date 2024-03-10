package edu.java.scrapper.database.dao;

import edu.java.scrapper.dao.ChatLinkDao;
import edu.java.scrapper.database.IntegrationTest;
import edu.java.scrapper.model.chatLink.ChatLink;
import java.util.ArrayList;
import java.util.List;
import edu.java.scrapper.model.chatLink.ChatLinkWithTgChat;
import edu.java.scrapper.model.chatLink.ChatLinkWithUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.database.UtilityDb.createChatLink;
import static edu.java.scrapper.database.UtilityDb.getAllFromChatLink;
import static edu.java.scrapper.database.UtilityDb.getIdFromChatByTgChatId;
import static edu.java.scrapper.database.UtilityDb.getIdFromLinkByUrl;
import static edu.java.scrapper.database.UtilityDb.insertRowIntoChat;
import static edu.java.scrapper.database.UtilityDb.insertRowIntoLink;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Rollback
public class JdbcChatLinkTest extends IntegrationTest {
    @Autowired
    private ChatLinkDao chatLinkDao;
    @Autowired
    public JdbcClient jdbcClient;

    private final String saveSQL = "INSERT INTO chat_link(chat_id, link_id) VALUES (%d, %d)";
    private final String defaultUrl = "defaultUrl";
    private final long defaultTgChatId = 10;

    @BeforeEach
    public void checkThatTableIsEmpty() {
        assertTrue(getAllFromChatLink(jdbcClient).isEmpty());
    }

    @Test
    @DisplayName("getByLinkId (В таблице chat_link 1 значение с искомой ссылкой)")
    void getByLinkIdWhenOneChat() {
        //Добавление в таблицы chat, link строк
        insertRowIntoChat(jdbcClient, defaultTgChatId);
        insertRowIntoLink(jdbcClient, defaultUrl);
        long chatId = getIdFromChatByTgChatId(jdbcClient, defaultTgChatId).orElseThrow();
        long linkId = getIdFromLinkByUrl(jdbcClient, defaultUrl).orElseThrow();

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
    @DisplayName("getByLinkId (В таблице chat_link несколько значений с искомой ссылкой)")
    void getByLinkIdWhenManyChats() {
        int count = 5;
        long linkId;
        List<Long> chatIds = new ArrayList<>();
        //Добавление в таблицы chat, link строк
        insertRowIntoLink(jdbcClient, defaultUrl);
        linkId = getIdFromLinkByUrl(jdbcClient, defaultUrl).orElseThrow();
        for (int i = 0; i < count; i++) {
            insertRowIntoChat(jdbcClient, defaultTgChatId + i);
            chatIds.add(getIdFromChatByTgChatId(jdbcClient, defaultTgChatId + i).orElseThrow());
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
    @DisplayName("getByLinkIdIdJoinChat (В таблице chat_link несколько значений с искомой ссылкой)")
    void getByLinkIdIdJoinChatWhenManyChats() {
        int count = 5;
        long linkId;
        List<Long> chatIds = new ArrayList<>();
        //Добавление в таблицы chat, link строк
        insertRowIntoLink(jdbcClient, defaultUrl);
        linkId = getIdFromLinkByUrl(jdbcClient, defaultUrl).orElseThrow();
        for (int i = 0; i < count; i++) {
            insertRowIntoChat(jdbcClient, defaultTgChatId + i);
            chatIds.add(getIdFromChatByTgChatId(jdbcClient, defaultTgChatId + i).orElseThrow());
        }

        //Добавление связи в chat_link
        for (long chatId : chatIds) {
            jdbcClient.sql(String.format(saveSQL, chatId, linkId))
                .update();
        }

        List<ChatLinkWithTgChat> contentByLinkId = chatLinkDao.getByLinkIdIdJoinChat(linkId);
        assertEquals(chatIds.size(), contentByLinkId.size());
        for (int i = 0; i < chatIds.size(); i++) {
            assertEquals(chatIds.get(i), contentByLinkId.get(i).getChatId());
            assertEquals(defaultTgChatId + i, contentByLinkId.get(i).getTgChatId());
        }
    }

    @Test
    void getByChatId() {
        //Добавление в таблицы chat, link строк
        insertRowIntoChat(jdbcClient, defaultTgChatId);
        insertRowIntoLink(jdbcClient, defaultUrl);
        long chatId = getIdFromChatByTgChatId(jdbcClient, defaultTgChatId).orElseThrow();
        long linkId = getIdFromLinkByUrl(jdbcClient, defaultUrl).orElseThrow();

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
    void getByChatIdJoinLink() {
        //Добавление в таблицы chat, link строк
        insertRowIntoChat(jdbcClient, defaultTgChatId);
        insertRowIntoLink(jdbcClient, defaultUrl);
        long chatId = getIdFromChatByTgChatId(jdbcClient, defaultTgChatId).orElseThrow();
        long linkId = getIdFromLinkByUrl(jdbcClient, defaultUrl).orElseThrow();

        //Добавление связи в chat_link
        jdbcClient.sql(String.format(saveSQL, chatId, linkId))
            .update();

        List<ChatLinkWithUrl> contentByChatId = chatLinkDao.getByChatIdJoinLink(chatId);
        assertAll(
            () -> assertEquals(1, contentByChatId.size()),
            () -> assertEquals(contentByChatId.getFirst().getLinkId(), linkId),
        () -> assertEquals(contentByChatId.getFirst().getUrl(), defaultUrl)
        );
    }

    @Test
    void getAll() {
        int count = 5;
        long linkId;
        List<Long> chatIds = new ArrayList<>();

        //Добавление в таблицы chat, link строк
        insertRowIntoLink(jdbcClient, defaultUrl);
        linkId = getIdFromLinkByUrl(jdbcClient, defaultUrl).orElseThrow();
        for (int i = 0; i < count; i++) {
            insertRowIntoChat(jdbcClient, defaultTgChatId + i);
            chatIds.add(getIdFromChatByTgChatId(jdbcClient, defaultTgChatId + i).orElseThrow());
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
    void save() {
        //Добавление в таблицы chat, link строк
        insertRowIntoChat(jdbcClient, defaultTgChatId);
        insertRowIntoLink(jdbcClient, defaultUrl);
        long chatId = getIdFromChatByTgChatId(jdbcClient, defaultTgChatId).orElseThrow();
        long linkId = getIdFromLinkByUrl(jdbcClient, defaultUrl).orElseThrow();

        //Добавление связи в chat_link
        chatLinkDao.save(createChatLink(chatId, linkId));

        List<ChatLink> content = getAllFromChatLink(jdbcClient);
        assertAll(
            "Поддтверждение, что появилась 1 связь",
            () -> assertFalse(content.isEmpty()),
            () -> assertEquals(content.getFirst().getLinkId(), linkId),
            () -> assertEquals(content.getFirst().getChatId(), chatId)
        );
    }

    @Test
    void remove() {
        //Добавление в таблицы chat, link строк
        insertRowIntoChat(jdbcClient, defaultTgChatId);
        insertRowIntoLink(jdbcClient, defaultUrl);
        long chatId = getIdFromChatByTgChatId(jdbcClient, defaultTgChatId).orElseThrow();
        long linkId = getIdFromLinkByUrl(jdbcClient, defaultUrl).orElseThrow();

        //Добавление связи в chat_link
        jdbcClient.sql(String.format(saveSQL, chatId, linkId))
            .update();
        List<ChatLink> content = getAllFromChatLink(jdbcClient);
        assertAll(
            "Поддтверждение, что появилась 1 связь",
            () -> assertFalse(content.isEmpty()),
            () -> assertEquals(content.getFirst().getLinkId(), linkId),
            () -> assertEquals(content.getFirst().getChatId(), chatId)
        );

        //Удаление связи
        chatLinkDao.delete(chatId, linkId);
        List<ChatLink> actualContent = getAllFromChatLink(jdbcClient);
        assertTrue(actualContent.isEmpty());
    }
}
