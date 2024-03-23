package edu.java.scrapper.database.dao.jpa;

import edu.java.scrapper.database.IntegrationTest;
import edu.java.scrapper.domain.jpa.dao.JpaChatRepository;
import edu.java.scrapper.domain.jpa.model.Chat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@Rollback
@Transactional
public class JpaChatTest extends IntegrationTest {

    @Autowired
    private JpaChatRepository chatRepository;

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    void findByTgChatId() {
        Optional<Chat> chat = chatRepository.findChatByTgChatId(defaultTgChatId);

        assertAll(
            () -> assertTrue(chat.isPresent()),
            () -> assertEquals(defaultTgChatId, chat.orElseThrow().getTgChatId())
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    void findById() {
        //Получениие чата с присвоенным id
        Optional<Chat> chat = chatRepository.findById(defaultId);

        assertAll(
            () -> assertTrue(chat.isPresent()),
            () -> assertEquals(defaultId, chat.orElseThrow().getId())
        );
    }

    @Test
    @Sql(value = "/sql/insertFiveRowChat.sql")
    void findAll() {
        int count = 5;
        List<Chat> actualChats = chatRepository.findAll();

        assertAll(
            () -> assertFalse(actualChats.isEmpty()),
            () -> assertEquals(count, actualChats.size())
        );
    }

    @Test
    void save() {
        long tgChatId = 15;
        //Добавление чата с заданным tgChatId
        Chat chat = new Chat();
        chat.setTgChatId(tgChatId);
        chat.setCreatedAt(OffsetDateTime.now());
        chatRepository.save(chat);

        Optional<Chat> actualChat = chatRepository.findChatByTgChatId(tgChatId);
        assertTrue(actualChat.isPresent());
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    void delete() {
        //Удаление чата с заданным tgChatId
        chatRepository.deleteByTgChatId(defaultTgChatId);

        Optional<Chat> actualChat = chatRepository.findChatByTgChatId(defaultTgChatId);
        assertTrue(actualChat.isEmpty());
    }
}
