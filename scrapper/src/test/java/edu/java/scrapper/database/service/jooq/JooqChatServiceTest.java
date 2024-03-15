package edu.java.scrapper.database.service.jooq;

import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.ReRegistrationException;
import edu.java.scrapper.database.IntegrationTest;
import edu.java.scrapper.domain.jooq.dao.JooqChatDao;
import edu.java.scrapper.domain.jooq.dao.JooqChatLinkDao;
import edu.java.scrapper.domain.jooq.dao.JooqLinkDao;
import java.time.OffsetDateTime;
import java.util.List;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.Chat;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.ChatLink;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.Link;
import edu.java.scrapper.service.JdbcAndJooq.jooq.JooqChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.database.dao.jooq.UtilityDbJooq.createChatLink;
import static edu.java.scrapper.database.dao.jooq.UtilityDbJooq.createLink;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Rollback
@Transactional
public class JooqChatServiceTest extends IntegrationTest {
    @Autowired
    private JooqChatService chatService;
    @Autowired
    private JooqChatDao chatDao;
    @Autowired
    private JooqLinkDao linkDao;
    @Autowired
    private JooqChatLinkDao chatLinkDao;

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

    @Test
    @DisplayName("Удаление чата и всех отслеживаемых ссылок")
    void unregisterChatAndDeleteLinks() {
        assertTrue(chatDao.getByTgChatId(tgChatId).isEmpty());
        chatDao.save(createChat());
        long chatId = chatDao.getByTgChatId(tgChatId).orElseThrow().getId();
        assertTrue(chatDao.getByTgChatId(tgChatId).isPresent());

        String url = "url";
        linkDao.save(createLink(url));
        long linkId1 = linkDao.getByUrl(url).orElseThrow().getId();
        linkDao.save(createLink(url + url));
        long linkId2 = linkDao.getByUrl(url + url).orElseThrow().getId();

        chatLinkDao.save(createChatLink(chatId, linkId1));
        chatLinkDao.save(createChatLink(chatId, linkId2));
        List<ChatLink> tempChatLink = chatLinkDao.getAll();
        assertEquals(2, tempChatLink.size());

        chatService.unregister(tgChatId);
        assertTrue(chatDao.getByTgChatId(tgChatId).isEmpty());
        List<ChatLink> actualChatLink = chatLinkDao.getAll();
        List<Link> actualLink = linkDao.getAll();
        assertAll(
            "Удален чат и все ссылки",
            () -> assertTrue(chatDao.getByTgChatId(tgChatId).isEmpty()),
            () -> assertEquals(0, actualChatLink.size()),
            () -> assertEquals(0, actualLink.size())
        );
    }
}
