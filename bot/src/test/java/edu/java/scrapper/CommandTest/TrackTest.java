package edu.java.scrapper.CommandTest;

import edu.java.bot.entity.Domain;
import edu.java.bot.entity.Link;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.handler.TrackCommand;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrackTest extends AbstractTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TrackCommand track;

    @Override
    protected void mockObjects(Long id, String command) {
        userRepository.saveUser(new User(id));
        super.mockObjects(id, command);
    }

    @Test
    @DisplayName("Добавление 1 поддерживаемой ссылки")
    void trackLinkWhenSupportedDomain() {
        Long chatId = new Random().nextLong();
        String link = "https://github.com/Ilnur37/Content-Checker";

        mockObjects(chatId, track.command() + " " + link);
        track.handle(update);

        var newLinks = userRepository.findLinksById(chatId);
        assertTrue(newLinks.isPresent());
        assertAll(
            () -> assertEquals(1, newLinks.get().size()),
            () -> assertTrue(
                newLinks.get().contains(new Link(new Domain("https://github.com"), "/Ilnur37/Content-Checker")))
        );
    }

    @Test
    @DisplayName("Добавление неподдерживаемой ссылки")
    void trackLinkWhenUnsupportedDomain() {
        Long chatId = new Random().nextLong();
        String link = "https://translate.google.com/?sl=ru&tl=en&op=translate";

        mockObjects(chatId, track.command() + " " + link);
        track.handle(update);

        var newLinks = userRepository.findLinksById(chatId);
        assertTrue(newLinks.isPresent());
        assertAll(
            () -> assertTrue(newLinks.get().isEmpty()),
            () -> assertFalse(
                newLinks.get()
                    .contains(new Link(new Domain("https://translate.google.com"), "/?sl=ru&tl=en&op=translate")))
        );
    }

    @Test
    @DisplayName("Добавление 2 поддерживаемых ссылок")
    void trackLinkWhen2SupportedLinks() {
        Long chatId = new Random().nextLong();
        String link1 = "https://github.com/Ilnur37/Content-Checker";
        String link2 = "https://github.com/Ilnur37/Tinkoff_Course";

        mockObjects(chatId, track.command() + " " + link1 + " " + link2);
        track.handle(update);

        var newLinks = userRepository.findLinksById(chatId);
        assertTrue(newLinks.isPresent());
        assertAll(
            () -> assertEquals(0, newLinks.get().size()),
            () -> assertFalse(
                newLinks.get().contains(new Link(new Domain("https://github.com"), "/Ilnur37/Content-Checker"))),
            () -> assertFalse(
                newLinks.get().contains(new Link(new Domain("https://github.com"), "/Ilnur37/Tinkoff_Course")))
        );
    }

    @Test
    @DisplayName("Повторное добавление поддерживаемой ссылки")
    void trackLinkWhenRepeatSupportedLinks() {
        Long chatId = new Random().nextLong();
        String link = "https://github.com/Ilnur37/Content-Checker";

        mockObjects(chatId, track.command() + " " + link);
        track.handle(update);

        var newLinks = userRepository.findLinksById(chatId);
        assertTrue(newLinks.isPresent());
        assertAll(
            () -> assertEquals(1, newLinks.get().size()),
            () -> assertTrue(
                newLinks.get().contains(new Link(new Domain("https://github.com"), "/Ilnur37/Content-Checker")))
        );
    }
}
