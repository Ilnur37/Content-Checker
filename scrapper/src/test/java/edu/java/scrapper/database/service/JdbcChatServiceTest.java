package edu.java.scrapper.database.service;

import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.ReRegistrationException;
import edu.java.scrapper.database.IntegrationTest;
import edu.java.scrapper.domain.jdbc.dao.ChatDao;
import edu.java.scrapper.domain.jdbc.dao.ChatLinkDao;
import edu.java.scrapper.domain.jdbc.dao.LinkDao;
import edu.java.scrapper.domain.jdbc.model.chat.Chat;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLink;
import edu.java.scrapper.domain.jdbc.model.link.Link;
import edu.java.scrapper.service.jdbc.JdbcChatService;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.database.UtilityDb.createChatLink;
import static edu.java.scrapper.database.UtilityDb.createLink;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @Autowired
    private LinkDao linkDao;
    @Autowired
    private ChatLinkDao chatLinkDao;
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
        assertTrue(chatDao.findByTgChatId(tgChatId).isEmpty());
        chatService.register(tgChatId);
        assertTrue(chatDao.findByTgChatId(tgChatId).isPresent());
    }

    @Test
    @DisplayName("Повторная регистрация")
    void reRegisterChat() {
        assertTrue(chatDao.findByTgChatId(tgChatId).isEmpty());
        chatDao.save(createChat());
        assertTrue(chatDao.findByTgChatId(tgChatId).isPresent());
        assertThrows(
            ReRegistrationException.class,
            () -> chatService.register(tgChatId)
        );
    }

    @Test
    @DisplayName("Удаление чата")
    void unregisterChat() {
        assertTrue(chatDao.findByTgChatId(tgChatId).isEmpty());
        chatDao.save(createChat());
        assertTrue(chatDao.findByTgChatId(tgChatId).isPresent());
        chatService.unregister(tgChatId);
        assertTrue(chatDao.findByTgChatId(tgChatId).isEmpty());
    }

    @Test
    @DisplayName("Удаление несуществующего чата")
    void unregisterChatWhenChatIdNotFound() {
        assertTrue(chatDao.findByTgChatId(tgChatId).isEmpty());
        assertThrows(
            ChatIdNotFoundException.class,
            () -> chatService.unregister(tgChatId)
        );
    }

    @Test
    @DisplayName("Удаление чата и всех отслеживаемых ссылок")
    void unregisterChatAndDeleteLinks() {
        assertTrue(chatDao.findByTgChatId(tgChatId).isEmpty());
        chatDao.save(createChat());
        long chatId = chatDao.findByTgChatId(tgChatId).orElseThrow().getId();
        assertTrue(chatDao.findByTgChatId(tgChatId).isPresent());

        String url = "url";
        linkDao.save(createLink(url));
        long linkId1 = linkDao.findByUrl(url).orElseThrow().getId();
        linkDao.save(createLink(url + url));
        long linkId2 = linkDao.findByUrl(url + url).orElseThrow().getId();

        chatLinkDao.save(createChatLink(chatId, linkId1));
        chatLinkDao.save(createChatLink(chatId, linkId2));
        List<ChatLink> tempChatLink = chatLinkDao.getAll();
        assertEquals(2, tempChatLink.size());

        chatService.unregister(tgChatId);
        assertTrue(chatDao.findByTgChatId(tgChatId).isEmpty());
        List<ChatLink> actualChatLink = chatLinkDao.getAll();
        List<Link> actualLink = linkDao.getAll();
        assertAll(
            "Удален чат и все ссылки",
            () -> assertTrue(chatDao.findByTgChatId(tgChatId).isEmpty()),
            () -> assertEquals(0, actualChatLink.size()),
            () -> assertEquals(0, actualLink.size())
        );
    }
}
