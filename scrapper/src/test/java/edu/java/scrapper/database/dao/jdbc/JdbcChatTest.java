package edu.java.scrapper.database.dao.jdbc;

import edu.java.scrapper.database.IntegrationTest;
import edu.java.scrapper.domain.jdbc.dao.JdbcChatDao;
import edu.java.scrapper.domain.jdbc.model.chat.Chat;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.domain.jdbc.model.chat.Chat.createChat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Rollback
@Transactional
public class JdbcChatTest extends IntegrationTest {

    @Autowired
    private ChatDao chatDao;

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    void findByTgChatId() {
        Optional<Chat> chat = chatDao.findByTgChatId(defaultTgChatId);

        assertAll(
            () -> assertTrue(chat.isPresent()),
            () -> assertEquals(defaultTgChatId, chat.orElseThrow().getTgChatId())
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    void findById() {
        long id = 1;

        //Получениие чата с присвоенным id
        Optional<Chat> chat = chatDao.findById(id);

        assertAll(
            () -> assertTrue(chat.isPresent()),
            () -> assertEquals(id, chat.orElseThrow().getId())
        );
    }

    @Test
    @Sql(value = "/sql/insertFiveRowChat.sql")
    void getAll() {
        int count = 5;
        List<Chat> actualChats = chatDao.getAll();

        assertAll(
            () -> assertFalse(actualChats.isEmpty()),
            () -> assertEquals(count, actualChats.size())
        );
    }

    @Test
    void save() {
        long tgChatId = 15;
        //Добавление чата с заданным tgChatId
        chatDao.save(createChat(tgChatId, OffsetDateTime.now()));

        Optional<Chat> actualChat = chatDao.findByTgChatId(tgChatId);
        assertTrue(actualChat.isPresent());
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    void delete() {
        //Удаление чата с заданным tgChatId
        chatDao.delete(defaultTgChatId);

        Optional<Chat> actualChat = chatDao.findByTgChatId(defaultTgChatId);
        assertTrue(actualChat.isEmpty());
    }
}
