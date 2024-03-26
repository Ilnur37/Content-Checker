package edu.java.scrapper.database.dao.jpa;

import edu.java.scrapper.database.JpaIntegrationTest;
import edu.java.scrapper.domain.jpa.model.Chat;
import edu.java.scrapper.domain.jpa.model.Link;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@Rollback
public class JpaChatLinkTest extends JpaIntegrationTest {

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    @DisplayName("В таблице chat_link 1 значение с искомой ссылкой")
    void getChatLinkWhenOneChat() {
        Chat chat = chatRepository.findChatByTgChatId(defaultTgChatId).orElseThrow();
        Link link = linkRepository.findLinkWithChatById(defaultId).orElseThrow();

        assertAll(
            () -> assertEquals(1, chat.getLinks().size()),
            () -> assertEquals(1, link.getChats().size())
        );
    }

    @Test
    @Sql(value = "/sql/insertFiveRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertFiveRowChatLink.sql")
    @DisplayName("В таблице chat_link несколько значений с искомой ссылкой")
    void getChatLinkWhenManyChats() {
        int count = 5;

        Link link = linkRepository.findLinkWithChatById(defaultId).orElseThrow();
        assertEquals(count, link.getChats().size());
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    void save() {
        //Добавление ссылки в chat
        Chat chat = chatRepository.findChatWithLinkById(defaultId).orElseThrow();
        String url = "url";
        Link link = new Link();
        link.setUrl(url);
        link.setCreatedAt(OffsetDateTime.now());
        link.setLastUpdateAt(OffsetDateTime.now());
        link.setAuthor(defaultAuthor);
        link.setName(defaultName);
        link.setLastCheckAt(OffsetDateTime.now());
        chat.addLink(link);

        Chat actualChat = chatRepository.findChatByTgChatId(defaultTgChatId).orElseThrow();
        Link actualLink = linkRepository.findLinkByUrl(url).orElseThrow();
        List<Link> actualLinks = linkRepository.findAll();

        assertAll(
            () -> assertEquals(2, actualChat.getLinks().size()),
            () -> assertEquals(2, actualLinks.size()),
            () -> assertEquals(1, actualLink.getChats().size())
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    void remove() {
        //Добавление ссылки в chat
        Chat chat = chatRepository.findChatWithLinkById(defaultId).orElseThrow();
        Link link = linkRepository.findLinkByUrl(defaultUrl).orElseThrow();
        chat.removeLink(link);

        Chat actualChat = chatRepository.findChatByTgChatId(defaultTgChatId).orElseThrow();
        Link actualLink = linkRepository.findLinkByUrl(defaultUrl).orElseThrow();
        List<Link> actualLinks = linkRepository.findAll();

        assertAll(
            () -> assertEquals(0, actualChat.getLinks().size()),
            () -> assertEquals(1, actualLinks.size()),
            () -> assertEquals(0, actualLink.getChats().size())
        );
    }
}
