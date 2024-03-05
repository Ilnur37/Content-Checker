package edu.java.scrapper.database;

import edu.java.scrapper.dao.ChatDao;
import edu.java.scrapper.model.chat.Chat;
import edu.java.scrapper.model.chat.ChatRowMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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
public class JdbcChatTest extends IntegrationTest {
    @Autowired
    private ChatDao chatDao;
    @Autowired
    public JdbcClient jdbcClient;
    @Autowired
    private ChatRowMapper chatRowMapper;
    private final String getAllSQL = "SELECT * FROM chat";
    private final String saveSQL = "INSERT INTO chat(tg_chat_id, created_at) VALUES (%d, CURRENT_TIMESTAMP)";
    private final long tgChatId = 10;

    private Chat createChat(long tgChatId) {
        Chat chat = new Chat();
        chat.setTgChatId(tgChatId);
        chat.setCreatedAt(OffsetDateTime.now());
        return chat;
    }

    @BeforeEach
    public void checkThatTableIsEmpty() {
        List<Chat> chats = jdbcClient.sql(getAllSQL)
            .query(chatRowMapper).list();
        assertTrue(chats.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void getByTgChatId() {
        //Добавление чата с заданным tgChatId
        jdbcClient.sql(String.format(saveSQL, tgChatId))
            .update();
        List<Chat> actualChats = jdbcClient.sql(getAllSQL)
            .query(chatRowMapper).list();
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
    @Transactional
    @Rollback
    void getById() {
        //Добавление чата с заданным tgChatId
        jdbcClient.sql(String.format(saveSQL, tgChatId))
            .update();
        List<Chat> actualChats = jdbcClient.sql(getAllSQL)
            .query(chatRowMapper).list();
        assertAll(
            "Поддтверждение, что появился 1 чат",
            () -> assertFalse(actualChats.isEmpty()),
            () -> assertEquals(actualChats.getFirst().getTgChatId(), tgChatId)
        );

        //Получениие чата с присвоенным id
        long id = actualChats.getFirst().getId();
        Optional<Chat> chat = chatDao.getById(id);
        assertAll(
            "Поддтверждение, что это только что добавленный чат",
            () -> assertTrue(chat.isPresent()),
            () -> assertEquals(chat.get().getId(), id)
        );
    }

    @Test
    @Transactional
    @Rollback
    void getAll() {
        long count = 10;

        //Добавление нескольких чатов
        for (long id = 1; id < 10; id++) {
            jdbcClient.sql(String.format(saveSQL, id))
                .update();
        }

        List<Chat> actualChats = chatDao.getAll();
        assertAll(
            "Поддтверждение, что чаты добавились",
            () -> assertFalse(actualChats.isEmpty()),
            () -> assertEquals(actualChats.size(), count - 1)
        );
    }

    @Test
    @Transactional
    @Rollback
    void save() {
        //Добавление чата с заданным tgChatId
        chatDao.save(createChat(tgChatId));

        List<Chat> actualChatsList = jdbcClient.sql(getAllSQL)
            .query(chatRowMapper).list();
        assertAll(
            () -> assertFalse(actualChatsList.isEmpty()),
            () -> assertEquals(actualChatsList.getFirst().getTgChatId(), tgChatId)
        );
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        //Добавление чата с заданным tgChatId
        jdbcClient.sql(String.format(saveSQL, tgChatId))
            .update();
        List<Chat> chats = jdbcClient.sql(getAllSQL)
            .query(chatRowMapper).list();
        assertAll(
            "Поддтверждение, что появился 1 чат",
            () -> assertFalse(chats.isEmpty()),
            () -> assertEquals(chats.getFirst().getTgChatId(), tgChatId)
        );

        //Удаление чата с заданным tgChatId
        chatDao.delete(createChat(tgChatId));
        List<Chat> actualChats = jdbcClient.sql(getAllSQL)
            .query(chatRowMapper).list();
        assertTrue(actualChats.isEmpty());
    }
}
