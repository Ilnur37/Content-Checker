package edu.java.scrapper.database.dao.jdbc;

import edu.java.scrapper.database.IntegrationTest;
import edu.java.scrapper.domain.jdbc.dao.JdbcLinkDao;
import edu.java.scrapper.domain.jdbc.model.link.Link;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.database.dao.jdbc.UtilityDbJdbc.createLink;
import static edu.java.scrapper.database.dao.jdbc.UtilityDbJdbc.getAllFromLink;
import static edu.java.scrapper.database.dao.jdbc.UtilityDbJdbc.insertRowIntoLink;
import static edu.java.scrapper.domain.jdbc.model.link.Link.createLink;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@Rollback
public class JdbcLinkTest extends IntegrationTest {

    @Autowired
    private LinkDao linkDao;

    @Test
    @Sql(value = "/sql/insertOneRowLink.sql")
    void getByUrl() {
        Optional<Link> link = linkDao.findByUrl(defaultUrl);

        assertAll(
            () -> assertTrue(link.isPresent()),
            () -> assertEquals(defaultUrl, link.get().getUrl())
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowLink.sql")
    void getById() {
        long id = 1;
        Optional<Link> link = linkDao.findById(id);

        assertAll(
            () -> assertTrue(link.isPresent()),
            () -> assertEquals(defaultUrl, link.get().getUrl())
        );
    }

    @Test
    @Sql(value = "/sql/insertFiveRowLink.sql")
    void getByLastUpdate() {
        int count = 5;
        List<Link> actualLinks = linkDao.getByLastUpdate(OffsetDateTime.now().plusSeconds(30));
        assertAll(
            "Подтверждение, что ссылки добавились",
            () -> assertFalse(actualLinks.isEmpty()),
            () -> assertEquals(2, actualLinks.size())
        );
    }

    @Test
    @Sql(value = "/sql/insertFiveRowLink.sql")
    void getAll() {
        int count = 5;
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
        linkDao.save(createLink(url, OffsetDateTime.now(), OffsetDateTime.now()));

        Optional<Link> actualLink = linkDao.findByUrl(url);
        assertTrue(actualLink.isPresent());
    }

    @Test
    @Sql(value = "/sql/insertOneRowLink.sql")
    void updateLastUpdateAtById() {
        //Изменение поля last_update_at
        OffsetDateTime newTime = OffsetDateTime.of(LocalDate.now(), LocalTime.of(12,12), ZoneOffset.UTC);
        linkDao.updateLastUpdateAtById(defaultId, newTime);

        Link actualLink = linkDao.findByUrl(defaultUrl).orElseThrow();
        assertEquals(newTime, actualLink.getLastUpdateAt());
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
}
