package edu.java.scrapper.database.service.jooq;

import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.ReRegistrationException;
import edu.java.scrapper.database.JooqIntegrationTest;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.ChatLink;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Rollback
@Transactional
public class JooqChatServiceTest extends JooqIntegrationTest {

    @Test
    @DisplayName("Регистрация чата")
    void registerChat() {
        chatService.register(defaultTgChatId);

        assertTrue(chatDao.findByTgChatId(defaultTgChatId).isPresent());
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @DisplayName("Повторная регистрация")
    void reRegisterChat() {
        assertTrue(chatDao.findByTgChatId(defaultTgChatId).isPresent());
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

        assertTrue(chatDao.findByTgChatId(defaultTgChatId).isEmpty());
    }

    @Test
    @DisplayName("Удаление несуществующего чата")
    void unregisterChatWhenChatIdNotFound() {
        assertTrue(chatDao.findByTgChatId(defaultTgChatId).isEmpty());
        assertThrows(
            ChatIdNotFoundException.class,
            () -> chatService.unregister(defaultTgChatId)
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    @DisplayName("Удаление чата и всех отслеживаемых ссылок")
    void unregisterChatAndDeleteLinks() {
        chatService.unregister(defaultTgChatId);

        assertTrue(chatDao.findByTgChatId(defaultTgChatId).isEmpty());
        List<ChatLink> actualChatLink = chatLinkDao.getAll();
        assertAll(
            "Удален чат и все ссылки",
            () -> assertTrue(chatDao.findByTgChatId(defaultTgChatId).isEmpty()),
            () -> assertEquals(0, actualChatLink.size())
        );
    }
}
