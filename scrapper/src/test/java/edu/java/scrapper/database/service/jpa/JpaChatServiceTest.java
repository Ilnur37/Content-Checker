package edu.java.scrapper.database.service.jpa;

import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.ReRegistrationException;
import edu.java.scrapper.database.JpaIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Rollback
@Transactional
public class JpaChatServiceTest extends JpaIntegrationTest {

    @Test
    @DisplayName("Регистрация чата")
    void registerChat() {
        chatService.register(defaultTgChatId);

        assertTrue(chatRepository.findChatByTgChatId(defaultTgChatId).isPresent());
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @DisplayName("Повторная регистрация")
    void reRegisterChat() {
        assertTrue(chatRepository.findChatByTgChatId(defaultTgChatId).isPresent());
        assertThrows(
            ReRegistrationException.class,
            () -> chatService.register(defaultTgChatId)
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @DisplayName("Удаление чата")
    void unregisterChat() {
        chatService.unregister(defaultTgChatId);

        assertTrue(chatRepository.findChatByTgChatId(defaultTgChatId).isEmpty());
    }

    @Test
    @DisplayName("Удаление несуществующего чата")
    void unregisterChatWhenChatIdNotFound() {
        assertTrue(chatRepository.findChatByTgChatId(defaultTgChatId).isEmpty());
        assertThrows(
            ChatIdNotFoundException.class,
            () -> chatService.unregister(defaultTgChatId)
        );
    }
}
