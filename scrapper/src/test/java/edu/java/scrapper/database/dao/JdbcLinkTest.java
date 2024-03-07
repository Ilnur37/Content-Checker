package edu.java.scrapper.database.dao;

import edu.java.scrapper.dao.LinkDao;
import edu.java.scrapper.database.IntegrationTest;
import edu.java.scrapper.model.link.Link;
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
import static edu.java.scrapper.database.dao.UtilityDb.getAllFromLink;
import static edu.java.scrapper.database.dao.UtilityDb.insertRowIntoLink;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Rollback
public class JdbcLinkTest extends IntegrationTest {
    @Autowired
    private LinkDao linkDao;
    @Autowired
    public JdbcClient jdbcClient;

    private final String defaultUrl = "defaultUrl";

    private Link createLink(String url) {
        Link link = new Link();
        link.setUrl(url);
        link.setCreatedAt(OffsetDateTime.now());
        link.setLastUpdateAt(OffsetDateTime.now());
        return link;
    }

    @BeforeEach
    public void checkThatTableIsEmpty() {
        assertTrue(getAllFromLink(jdbcClient).isEmpty());
    }

    @Test
    void getByUrl() {
        //Добавление ссылки с заданным url
        insertRowIntoLink(jdbcClient, defaultUrl);
        List<Link> actualLinks = getAllFromLink(jdbcClient);
        assertAll(
            "Поддтверждение, что появилась 1 ссылка",
            () -> assertFalse(actualLinks.isEmpty()),
            () -> assertEquals(actualLinks.getFirst().getUrl(), defaultUrl)
        );

        //Получениие ссылки с заданным url
        Optional<Link> link = linkDao.getByUrl(defaultUrl);
        assertAll(
            "Поддтверждение, что это только что добавленная ссылка",
            () -> assertTrue(link.isPresent()),
            () -> assertEquals(link.get().getUrl(), defaultUrl)
        );
    }

    @Test
    void getById() {
        //Добавление ссылки с заданным url
        insertRowIntoLink(jdbcClient, defaultUrl);
        List<Link> actualLinks = getAllFromLink(jdbcClient);
        assertAll(
            "Поддтверждение, что появилась 1 ссылка",
            () -> assertFalse(actualLinks.isEmpty()),
            () -> assertEquals(actualLinks.getFirst().getUrl(), defaultUrl)
        );

        //Получениие ссылки с присвоенным id
        long id = actualLinks.getFirst().getId();
        Optional<Link> link = linkDao.getById(id);
        assertAll(
            "Поддтверждение, что это только что добавленная ссылка",
            () -> assertTrue(link.isPresent()),
            () -> assertEquals(link.get().getUrl(), defaultUrl)
        );
    }

    @Test
    void getAll() {
        //Добавление нескольких ссылок
        long count = 10;
        for (long id = 1; id < 10; id++) {
            insertRowIntoLink(jdbcClient, defaultUrl + id);
        }

        List<Link> actualLinks = linkDao.getAll();
        assertAll(
            "Поддтверждение, что ссылки добавились",
            () -> assertFalse(actualLinks.isEmpty()),
            () -> assertEquals(actualLinks.size(), count - 1)
        );
    }

    @Test
    void save() {
        //Добавление ссылки с заданным url
        linkDao.save(createLink(defaultUrl));

        List<Link> actualLinkList = getAllFromLink(jdbcClient);
        assertAll(
            () -> assertFalse(actualLinkList.isEmpty()),
            () -> assertEquals(actualLinkList.getFirst().getUrl(), defaultUrl)
        );
    }

    @Test
    void remove() {
        //Добавление ссылки с заданным url
        insertRowIntoLink(jdbcClient, defaultUrl);
        List<Link> links = getAllFromLink(jdbcClient);
        assertAll(
            "Поддтверждение, что появилась 1 ссылка",
            () -> assertFalse(links.isEmpty()),
            () -> assertEquals(links.getFirst().getUrl(), defaultUrl)
        );

        //Удаление ссылки с заданным url
        linkDao.delete(createLink(defaultUrl));
        List<Link> actualLinks = getAllFromLink(jdbcClient);
        assertTrue(actualLinks.isEmpty());
    }
}
