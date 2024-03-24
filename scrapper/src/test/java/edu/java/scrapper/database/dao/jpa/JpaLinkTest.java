package edu.java.scrapper.database.dao.jpa;

import edu.java.scrapper.database.JpaIntegrationTest;
import edu.java.scrapper.domain.jpa.model.Link;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@Rollback
public class JpaLinkTest extends JpaIntegrationTest {

    @Test
    @Sql(value = "/sql/insertOneRowLink.sql")
    void getByUrl() {
        //Получениие ссылки с заданным url
        Optional<Link> link = linkRepository.findLinkByUrl(defaultUrl);

        assertAll(
            () -> assertTrue(link.isPresent()),
            () -> assertEquals(defaultUrl, link.orElseThrow().getUrl())
        );
    }

    @Test
    @Sql(value = "/sql/insertOneRowLink.sql")
    void getById() {
        Optional<Link> link = linkRepository.findById(defaultId);

        assertAll(
            () -> assertTrue(link.isPresent()),
            () -> assertEquals(defaultUrl, link.orElseThrow().getUrl())
        );
    }

    @Test
    @Sql(value = "/sql/insertFiveRowLink.sql")
    void getByLastCheck() {
        List<Link> actualLinks = linkRepository.findLinksByLastCheckAtBefore(OffsetDateTime.now().plusSeconds(30));

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
        List<Link> actualLinks = linkRepository.findAll();

        assertAll(
            () -> assertFalse(actualLinks.isEmpty()),
            () -> assertEquals(count, actualLinks.size())
        );
    }

    @Test
    void save() {
        //Добавление ссылки с заданным url
        String url = "url";
        Link link = Link.createLink(url, defaultName, defaultAuthor, OffsetDateTime.now());
        linkRepository.save(link);

        List<Link> actualLinkList = linkRepository.findAll();
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
        linkRepository.updateLastUpdateAtById(defaultId, newTime);

        Link actualLink = linkRepository.findLinkByUrl(defaultUrl).orElseThrow();
        assertTrue(newTime.isEqual(actualLink.getLastUpdateAt()));
    }

    @Test
    @Sql(value = "/sql/insertOneRowLink.sql")
    void deleteByUrl() {
        //Удаление ссылки с заданным url
        linkRepository.deleteByUrl(defaultUrl);

        Optional<Link> actualLink = linkRepository.findLinkByUrl(defaultUrl);
        assertTrue(actualLink.isEmpty());
    }

    @Test
    @Sql(value = "/sql/insertOneRowLink.sql")
    void deleteById() {
        //Удаление ссылки с заданным id
        linkRepository.deleteById(defaultId);

        Optional<Link> actualLink = linkRepository.findLinkByUrl(defaultUrl);
        assertTrue(actualLink.isEmpty());
    }

    @Test
    @Sql(value = "/sql/insertFiveRowLink.sql")
    void deleteUnnecessary() {
        linkRepository.deleteUnnecessary();
        List<Link> actualLinks = linkRepository.findAll();

        assertTrue(actualLinks.isEmpty());
    }
}
