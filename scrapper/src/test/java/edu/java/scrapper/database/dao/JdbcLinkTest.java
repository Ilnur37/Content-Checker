package edu.java.scrapper.database.dao;

import edu.java.scrapper.domain.jdbc.dao.LinkDao;
import edu.java.scrapper.database.IntegrationTest;
import edu.java.scrapper.domain.jdbc.model.link.Link;
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
import static edu.java.scrapper.database.UtilityDb.createLink;
import static edu.java.scrapper.database.UtilityDb.getAllFromLink;
import static edu.java.scrapper.database.UtilityDb.insertRowIntoLink;
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
    void getByLustUpdate() {
        //Добавление нескольких ссылок
        OffsetDateTime now = OffsetDateTime.now();
        String sql = "INSERT INTO link(url, created_at, last_update_at) VALUES (?, CURRENT_TIMESTAMP, ?)";
        long count = 10;
        for (long id = 1; id <= 10; id++) {
            jdbcClient.sql(sql)
                .params(defaultUrl + id, now.plusSeconds(5 * id))
                .update();
        }

        List<Link> actualLinks = linkDao.getByLustUpdate(now.plusSeconds(25));
        assertAll(
            "Поддтверждение, что ссылки добавились",
            () -> assertFalse(actualLinks.isEmpty()),
            () -> assertEquals(actualLinks.size(), count/2 - 1)
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
    void updateLastUpdateAtById() {
        //Добавление ссылки с заданным url
        insertRowIntoLink(jdbcClient, defaultUrl);
        List<Link> links = getAllFromLink(jdbcClient);
        assertAll(
            "Поддтверждение, что появилась 1 ссылка",
            () -> assertFalse(links.isEmpty()),
            () -> assertEquals(links.getFirst().getUrl(), defaultUrl)
        );

        //Изменение поля last_update_at
        linkDao.updateLastUpdateAtById(links.getFirst().getId(), OffsetDateTime.MAX);
        List<Link> actualLinks = getAllFromLink(jdbcClient);
        assertEquals(actualLinks.getFirst().getLastUpdateAt(), OffsetDateTime.MAX);
    }

    @Test
    void deleteByUrl() {
        //Добавление ссылки с заданным url
        insertRowIntoLink(jdbcClient, defaultUrl);
        List<Link> links = getAllFromLink(jdbcClient);
        assertAll(
            "Поддтверждение, что появилась 1 ссылка",
            () -> assertFalse(links.isEmpty()),
            () -> assertEquals(links.getFirst().getUrl(), defaultUrl)
        );

        //Удаление ссылки с заданным url
        linkDao.deleteByUrl(defaultUrl);
        List<Link> actualLinks = getAllFromLink(jdbcClient);
        assertTrue(actualLinks.isEmpty());
    }

    @Test
    void deleteById() {
        //Добавление ссылки с заданным url
        insertRowIntoLink(jdbcClient, defaultUrl);
        List<Link> links = getAllFromLink(jdbcClient);
        assertAll(
            "Поддтверждение, что появилась 1 ссылка",
            () -> assertFalse(links.isEmpty()),
            () -> assertEquals(links.getFirst().getUrl(), defaultUrl)
        );

        //Удаление ссылки с заданным id
        linkDao.deleteById(links.getFirst().getId());
        List<Link> actualLinks = getAllFromLink(jdbcClient);
        assertTrue(actualLinks.isEmpty());
    }
}
