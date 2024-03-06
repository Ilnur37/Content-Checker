package edu.java.scrapper.database.service;

import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.scrapper.dao.ChatDao;
import edu.java.scrapper.database.IntegrationTest;
import edu.java.scrapper.exception.custom.ReRegistrationException;
import edu.java.scrapper.model.chat.Chat;
import edu.java.scrapper.service.jdbc.JdbcChatService;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Rollback
@Transactional
public class JdbcChatServiceTest extends IntegrationTest {
    @Autowired
    private JdbcChatService chatService;
    @Autowired
    private ChatDao chatDao;
    private final long tgChatId = 10;

    private Chat createChat() {
        Chat chat = new Chat();
        chat.setTgChatId(tgChatId);
        chat.setCreatedAt(OffsetDateTime.now());
        return chat;
    }

    @Test
    @DisplayName("Регистрация чата")
    void registerChat() {
        assertTrue(chatDao.getByTgChatId(tgChatId).isEmpty());
        chatService.register(tgChatId);
        assertTrue(chatDao.getByTgChatId(tgChatId).isPresent());
    }

    @Test
    @DisplayName("Повторная регистрация")
    void reRegisterChat() {
        assertTrue(chatDao.getByTgChatId(tgChatId).isEmpty());
        chatDao.save(createChat());
        assertTrue(chatDao.getByTgChatId(tgChatId).isPresent());
        assertThrows(
            ReRegistrationException.class,
            () -> chatService.register(tgChatId)
        );
    }

    @Test
    @DisplayName("Удаление чата")
    void unregisterChat() {
        assertTrue(chatDao.getByTgChatId(tgChatId).isEmpty());
        chatDao.save(createChat());
        assertTrue(chatDao.getByTgChatId(tgChatId).isPresent());
        chatService.unregister(tgChatId);
        assertTrue(chatDao.getByTgChatId(tgChatId).isEmpty());
    }

    @Test
    @DisplayName("Удаление несуществующего чата")
    void unregisterChatWhenChatIdNotFound() {
        assertTrue(chatDao.getByTgChatId(tgChatId).isEmpty());
        assertThrows(
            ChatIdNotFoundException.class,
            () -> chatService.unregister(tgChatId)
        );
    }
}
