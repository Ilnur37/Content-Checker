package edu.java.scrapper.database.dao.jdbc;

import edu.java.scrapper.database.IntegrationTest;
import edu.java.scrapper.domain.jdbc.dao.JdbcChatDao;
import edu.java.scrapper.domain.jdbc.model.chat.Chat;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.database.dao.jdbc.UtilityDbJdbc.createChat;
import static edu.java.scrapper.database.dao.jdbc.UtilityDbJdbc.getAllFromChat;
import static edu.java.scrapper.database.dao.jdbc.UtilityDbJdbc.insertRowIntoChat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Rollback
@Transactional
public class JdbcChatTest extends IntegrationTest {
    @Autowired
    private JdbcChatDao chatDao;
    @Autowired
    private JdbcClient jdbcClient;

    private final long tgChatId = 10;

    @BeforeEach
    public void checkThatTableIsEmpty() {
        assertTrue(getAllFromChat(jdbcClient).isEmpty());
    }

    @Test
    void getByTgChatId() {
        //Добавление чата с заданным tgChatId
        insertRowIntoChat(jdbcClient, tgChatId);
        List<Chat> actualChats = getAllFromChat(jdbcClient);
        assertAll(
            "Поддтверждение, что появился 1 чат",
            () -> assertFalse(actualChats.isEmpty()),
            () -> assertEquals(actualChats.getFirst().getTgChatId(), tgChatId)
        );

        //Получениие чата с заданным tgChatId
        Optional<Chat> chat = chatDao.getByTgChatId(tgChatId);
        assertAll(
            "Поддтверждение, что это только что добавленный чат",
            () -> assertTrue(chat.isPresent()),
            () -> assertEquals(chat.get().getTgChatId(), tgChatId)
        );
    }

    @Test
    void getById() {
        //Добавление чата с заданным tgChatId
        insertRowIntoChat(jdbcClient, tgChatId);
        List<Chat> actualChats = getAllFromChat(jdbcClient);
        assertAll(
            "Поддтверждение, что появился 1 чат",
            () -> assertFalse(actualChats.isEmpty()),
            () -> assertEquals(actualChats.getFirst().getTgChatId(), tgChatId)
        );
        long id = actualChats.getFirst().getId();

        //Получениие чата с присвоенным id
        Optional<Chat> chat = chatDao.getById(id);
        assertAll(
            "Поддтверждение, что это только что добавленный чат",
            () -> assertTrue(chat.isPresent()),
            () -> assertEquals(chat.get().getId(), id)
        );
    }

    @Test
    void getAll() {
        //Добавление нескольких чатов
        long count = 10;
        for (long id = 1; id < count; id++) {
            insertRowIntoChat(jdbcClient, id);
        }

        List<Chat> actualChats = chatDao.getAll();
        assertAll(
            "Поддтверждение, что чаты добавились",
            () -> assertFalse(actualChats.isEmpty()),
            () -> assertEquals(actualChats.size(), count - 1)
        );
    }

    @Test
    void save() {
        //Добавление чата с заданным tgChatId
        chatDao.save(createChat(tgChatId));

        List<Chat> actualChatsList = getAllFromChat(jdbcClient);
        assertAll(
            () -> assertFalse(actualChatsList.isEmpty()),
            () -> assertEquals(actualChatsList.getFirst().getTgChatId(), tgChatId)
        );
    }

    @Test
    void delete() {
        //Добавление чата с заданным tgChatId
        insertRowIntoChat(jdbcClient, tgChatId);
        List<Chat> chats = getAllFromChat(jdbcClient);
        assertAll(
            "Поддтверждение, что появился 1 чат",
            () -> assertFalse(chats.isEmpty()),
            () -> assertEquals(chats.getFirst().getTgChatId(), tgChatId)
        );

        //Удаление чата с заданным tgChatId
        chatDao.delete(tgChatId);
        List<Chat> actualChats = getAllFromChat(jdbcClient);
        assertTrue(actualChats.isEmpty());
    }
}
