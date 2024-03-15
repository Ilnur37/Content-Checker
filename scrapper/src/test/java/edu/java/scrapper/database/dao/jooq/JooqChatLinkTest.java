package edu.java.scrapper.database.dao.jooq;

import edu.java.scrapper.database.IntegrationTest;
import edu.java.scrapper.domain.jooq.dao.JooqChatLinkDao;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.ChatLink;
import edu.java.scrapper.domain.model.ChatLinkWithUrl;
import java.util.ArrayList;
import java.util.List;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.database.dao.jooq.UtilityDbJooq.createChatLink;
import static edu.java.scrapper.database.dao.jooq.UtilityDbJooq.getAllFromChatLink;
import static edu.java.scrapper.database.dao.jooq.UtilityDbJooq.getIdFromChatByTgChatId;
import static edu.java.scrapper.database.dao.jooq.UtilityDbJooq.getIdFromLinkByUrl;
import static edu.java.scrapper.database.dao.jooq.UtilityDbJooq.insertRowIntoChat;
import static edu.java.scrapper.database.dao.jooq.UtilityDbJooq.insertRowIntoChatLink;
import static edu.java.scrapper.database.dao.jooq.UtilityDbJooq.insertRowIntoLink;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Rollback
public class JooqChatLinkTest extends IntegrationTest {
    @Autowired
    private JooqChatLinkDao chatLinkDao;
    @Autowired
    private DSLContext dsl;
    private final String defaultUrl = "defaultUrl";
    private final long defaultTgChatId = 10;

    @BeforeEach
    public void checkThatTableIsEmpty() {
        assertTrue(getAllFromChatLink(dsl).isEmpty());
    }

    @Test
    @DisplayName("getByLinkId (В таблице chat_link 1 значение с искомой ссылкой)")
    void getByLinkIdWhenOneChat() {
        //Добавление в таблицы chat, link строк
        insertRowIntoChat(dsl, defaultTgChatId);
        insertRowIntoLink(dsl, defaultUrl);
        long chatId = getIdFromChatByTgChatId(dsl, defaultTgChatId).orElseThrow();
        long linkId = getIdFromLinkByUrl(dsl, defaultUrl).orElseThrow();

        //Добавление связи в chat_link
        insertRowIntoChatLink(dsl, chatId, linkId);

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
        insertRowIntoLink(dsl, defaultUrl);
        linkId = getIdFromLinkByUrl(dsl, defaultUrl).orElseThrow();
        for (int i = 0; i < count; i++) {
            insertRowIntoChat(dsl, defaultTgChatId + i);
            chatIds.add(getIdFromChatByTgChatId(dsl, defaultTgChatId + i).orElseThrow());
        }

        //Добавление связи в chat_link
        for (long chatId : chatIds) {
            insertRowIntoChatLink(dsl, chatId, linkId);
        }

        List<ChatLink> contentByLinkId = chatLinkDao.getByLinkId(linkId);
        assertEquals(chatIds.size(), contentByLinkId.size());
        for (int i = 0; i < chatIds.size(); i++) {
            assertEquals(chatIds.get(i), contentByLinkId.get(i).getChatId());
        }
    }

   /* @Test
    @DisplayName("getByLinkIdIdJoinChat (В таблице chat_link несколько значений с искомой ссылкой)")
    void getByLinkIdIdJoinChatWhenManyChats() {
        int count = 5;
        long linkId;
        List<Long> chatIds = new ArrayList<>();
        //Добавление в таблицы chat, link строк
        insertRowIntoLink(dsl, defaultUrl);
        linkId = getIdFromLinkByUrl(dsl, defaultUrl).orElseThrow();
        for (int i = 0; i < count; i++) {
            insertRowIntoChat(dsl, defaultTgChatId + i);
            chatIds.add(getIdFromChatByTgChatId(dsl, defaultTgChatId + i).orElseThrow());
        }

        //Добавление связи в chat_link
        for (long chatId : chatIds) {
            dsl.sql(String.format(saveSQL, chatId, linkId))
                .update();
        }

        List<ChatLinkWithTgChat> contentByLinkId = chatLinkDao.getByLinkIdIdJoinChat(linkId);
        assertEquals(chatIds.size(), contentByLinkId.size());
        for (int i = 0; i < chatIds.size(); i++) {
            assertEquals(chatIds.get(i), contentByLinkId.get(i).getChatId());
            assertEquals(defaultTgChatId + i, contentByLinkId.get(i).getTgChatId());
        }
    }*/

    @Test
    void getByChatId() {
        //Добавление в таблицы chat, link строк
        insertRowIntoChat(dsl, defaultTgChatId);
        insertRowIntoLink(dsl, defaultUrl);
        long chatId = getIdFromChatByTgChatId(dsl, defaultTgChatId).orElseThrow();
        long linkId = getIdFromLinkByUrl(dsl, defaultUrl).orElseThrow();

        //Добавление связи в chat_link
        insertRowIntoChatLink(dsl, chatId, linkId);

        List<ChatLink> contentByChatId = chatLinkDao.getByChatId(chatId);
        assertAll(
            () -> assertEquals(1, contentByChatId.size()),
            () -> assertEquals(contentByChatId.getFirst().getLinkId(), linkId)
        );
    }

    @Test
    void getByChatIdJoinLink() {
        //Добавление в таблицы chat, link строк
        insertRowIntoChat(dsl, defaultTgChatId);
        insertRowIntoLink(dsl, defaultUrl);
        long chatId = getIdFromChatByTgChatId(dsl, defaultTgChatId).orElseThrow();
        long linkId = getIdFromLinkByUrl(dsl, defaultUrl).orElseThrow();

        //Добавление связи в chat_link
        insertRowIntoChatLink(dsl, chatId, linkId);

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
        insertRowIntoLink(dsl, defaultUrl);
        linkId = getIdFromLinkByUrl(dsl, defaultUrl).orElseThrow();
        for (int i = 0; i < count; i++) {
            insertRowIntoChat(dsl, defaultTgChatId + i);
            chatIds.add(getIdFromChatByTgChatId(dsl, defaultTgChatId + i).orElseThrow());
        }

        //Добавление связи в chat_link
        for (long chatId : chatIds) {
            insertRowIntoChatLink(dsl, chatId, linkId);
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
        insertRowIntoChat(dsl, defaultTgChatId);
        insertRowIntoLink(dsl, defaultUrl);
        long chatId = getIdFromChatByTgChatId(dsl, defaultTgChatId).orElseThrow();
        long linkId = getIdFromLinkByUrl(dsl, defaultUrl).orElseThrow();

        //Добавление связи в chat_link
        chatLinkDao.save(createChatLink(chatId, linkId));

        List<ChatLink> content = getAllFromChatLink(dsl);
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
        insertRowIntoChat(dsl, defaultTgChatId);
        insertRowIntoLink(dsl, defaultUrl);
        long chatId = getIdFromChatByTgChatId(dsl, defaultTgChatId).orElseThrow();
        long linkId = getIdFromLinkByUrl(dsl, defaultUrl).orElseThrow();

        //Добавление связи в chat_link
        insertRowIntoChatLink(dsl, chatId, linkId);
        List<ChatLink> content = getAllFromChatLink(dsl);
        assertAll(
            "Поддтверждение, что появилась 1 связь",
            () -> assertFalse(content.isEmpty()),
            () -> assertEquals(content.getFirst().getLinkId(), linkId),
            () -> assertEquals(content.getFirst().getChatId(), chatId)
        );

        //Удаление связи
        chatLinkDao.delete(chatId, linkId);
        List<ChatLink> actualContent = getAllFromChatLink(dsl);
        assertTrue(actualContent.isEmpty());
    }
}
