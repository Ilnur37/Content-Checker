package edu.java.scrapper.database.dao.jooq;

import edu.java.scrapper.database.IntegrationTest;
import edu.java.scrapper.domain.jooq.dao.JooqLinkDao;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.Link;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@Rollback
public class JooqLinkTest extends IntegrationTest {

    @Autowired
    JooqLinkDao linkDao;

    @Test
    @Sql(value = "/sql/insertOneRowLink.sql")
    void getByUrl() {
        //Получениие ссылки с заданным url
        Optional<Link> link = linkDao.findByUrl(defaultUrl);

        assertAll(
            () -> assertTrue(link.isPresent()),
            () -> assertEquals(defaultUrl, link.orElseThrow().getUrl())
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowLink.sql")
    void getById() {
        Optional<Link> link = linkDao.findById(defaultId);

        assertAll(
            () -> assertTrue(link.isPresent()),
            () -> assertEquals(defaultUrl, link.orElseThrow().getUrl())
        );
    }

    @Test
    @Sql(value = "/sql/insertFiveRowLink.sql")
    void getByLastCheck() {
        List<Link> actualLinks = linkDao.getByLastCheck(OffsetDateTime.now().plusSeconds(30));

        assertAll(
            () -> assertFalse(actualLinks.isEmpty()),
            () -> assertEquals(2, actualLinks.size())
        );
    }

    @Test
    @Sql(value = "/sql/insertFiveRowLink.sql")
    void getAll() {
        //Добавление нескольких ссылок
        long count = 5;
        List<Link> actualLinks = linkDao.getAll();

        assertAll(
            () -> assertFalse(actualLinks.isEmpty()),
            () -> assertEquals(count, actualLinks.size())
        );
    }

    @Test
    void save() {
        //Добавление ссылки с заданным url
        String url = "url";
        Link link = new Link();
        link.setUrl(url);
        link.setCreatedAt(OffsetDateTime.now());
        link.setLastUpdateAt(OffsetDateTime.now());
        link.setAuthor(defaultAuthor);
        link.setName(defaultName);
        link.setLastCheckAt(OffsetDateTime.now());
        linkDao.save(link);

        List<Link> actualLinkList = linkDao.getAll();
        assertAll(
            () -> assertFalse(actualLinkList.isEmpty()),
            () -> assertEquals(url, actualLinkList.getFirst().getUrl())
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowLink.sql")
    void updateLastUpdateAtById() {
        //Изменение поля last_update_at
        OffsetDateTime newTime = OffsetDateTime.of(LocalDate.now(), LocalTime.of(12, 12), ZoneOffset.UTC);
        linkDao.updateLastUpdateAtById(defaultId, newTime);

        Link actualLink = linkDao.findByUrl(defaultUrl).orElseThrow();
        assertTrue(newTime.isEqual(actualLink.getLastUpdateAt()));
    }

    @Test
    @Sql(value = "/sql/insertOneRowLink.sql")
    void deleteByUrl() {
        //Удаление ссылки с заданным url
        linkDao.deleteByUrl(defaultUrl);

        Optional<Link> actualLink = linkDao.findByUrl(defaultUrl);
        assertTrue(actualLink.isEmpty());
    }

    @Test
    @Sql(value = "/sql/insertOneRowLink.sql")
    void deleteById() {
        //Удаление ссылки с заданным id
        linkDao.deleteById(defaultId);

        Optional<Link> actualLink = linkDao.findByUrl(defaultUrl);
        assertTrue(actualLink.isEmpty());
    }

    @Test
    @Sql(value = "/sql/insertFiveRowLink.sql")
    void deleteUnnecessary() {
        linkDao.deleteUnnecessary();
        List<Link> actualLinks = linkDao.getAll();

        assertTrue(actualLinks.isEmpty());
    }
}
