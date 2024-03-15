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
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.database.dao.jooq.UtilityDbJooq.createLink;
import static edu.java.scrapper.database.dao.jooq.UtilityDbJooq.getAllFromLink;
import static edu.java.scrapper.database.dao.jooq.UtilityDbJooq.insertRowIntoLink;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Rollback
public class JooqLinkTest extends IntegrationTest {
    @Autowired
    JooqLinkDao linkDao;
    @Autowired
    private DSLContext dsl;
    private final String defaultUrl = "defaultUrl";

    @BeforeEach
    public void checkThatTableIsEmpty() {
        assertTrue(getAllFromLink(dsl).isEmpty());
    }

    @Test
    void getByUrl() {
        //Добавление ссылки с заданным url
        insertRowIntoLink(dsl, defaultUrl);
        List<Link> actualLinks = getAllFromLink(dsl);
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
        insertRowIntoLink(dsl, defaultUrl);
        List<Link> actualLinks = getAllFromLink(dsl);
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
            insertRowIntoLink(dsl, defaultUrl + id);
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

        List<Link> actualLinkList = getAllFromLink(dsl);
        assertAll(
            () -> assertFalse(actualLinkList.isEmpty()),
            () -> assertEquals(actualLinkList.getFirst().getUrl(), defaultUrl)
        );
    }

    @Test
    void updateLastUpdateAtById() {
        //Добавление ссылки с заданным url
        insertRowIntoLink(dsl, defaultUrl);
        List<Link> links = getAllFromLink(dsl);
        assertAll(
            "Поддтверждение, что появилась 1 ссылка",
            () -> assertFalse(links.isEmpty()),
            () -> assertEquals(links.getFirst().getUrl(), defaultUrl)
        );

        //Изменение поля last_update_at
        OffsetDateTime newTime = OffsetDateTime.of(LocalDate.now(), LocalTime.of(10, 10), ZoneOffset.UTC);
        linkDao.updateLastUpdateAtById(links.getFirst().getId(), newTime);
        List<Link> actualLinks = getAllFromLink(dsl);
        assertTrue(actualLinks.getFirst().getLastUpdateAt().isEqual(newTime));
    }

    @Test
    void deleteByUrl() {
        //Добавление ссылки с заданным url
        insertRowIntoLink(dsl, defaultUrl);
        List<Link> links = getAllFromLink(dsl);
        assertAll(
            "Поддтверждение, что появилась 1 ссылка",
            () -> assertFalse(links.isEmpty()),
            () -> assertEquals(links.getFirst().getUrl(), defaultUrl)
        );

        //Удаление ссылки с заданным url
        linkDao.deleteByUrl(defaultUrl);
        List<Link> actualLinks = getAllFromLink(dsl);
        assertTrue(actualLinks.isEmpty());
    }

    @Test
    void deleteById() {
        //Добавление ссылки с заданным url
        insertRowIntoLink(dsl, defaultUrl);
        List<Link> links = getAllFromLink(dsl);
        assertAll(
            "Поддтверждение, что появилась 1 ссылка",
            () -> assertFalse(links.isEmpty()),
            () -> assertEquals(links.getFirst().getUrl(), defaultUrl)
        );

        //Удаление ссылки с заданным id
        linkDao.deleteById(links.getFirst().getId());
        List<Link> actualLinks = getAllFromLink(dsl);
        assertTrue(actualLinks.isEmpty());
    }
}
