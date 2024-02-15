package CommandTest;

import edu.java.bot.entity.Domain;
import edu.java.bot.entity.Link;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.handler.UnTrackCommand;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnTrackTest extends AbstractTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UnTrackCommand unTrack;

    @Override
    protected void mockObjects(Long id, String command) {
        userRepository.saveUser(new User(id));
        userRepository.saveLink(id, new Link(new Domain("https://github.com"), "/Ilnur37/Content-Checker"));
        super.mockObjects(id, command);
    }

    @Test
    @DisplayName("Удаление 1 отслеживаемой ссылки")
    void unTrackLinkWhenContainLink() {
        Long chatId = new Random().nextLong();
        String link = "https://github.com/Ilnur37/Content-Checker";
        Link expectedLink = new Link(new Domain("https://github.com"), "/Ilnur37/Content-Checker");

        mockObjects(chatId, unTrack.command() + " " + link);
        assertTrue(userRepository.findLinksById(chatId).get().contains(expectedLink));
        unTrack.handle(update);
        assertFalse(userRepository.findLinksById(chatId).get().contains(expectedLink));
    }

    @Test
    @DisplayName("Удаление неотслеживаемой ссылки")
    void unTrackLinkWhenNoContainLink() {
        Long chatId = new Random().nextLong();
        String link = "https://github.com/Ilnur37/Content-Checker";
        Link link1 = new Link(new Domain("https://github.com"), "/Ilnur37");

        mockObjects(chatId, unTrack.command() + " " + link);
        var oldLinks = userRepository.findLinksById(chatId);
        assertFalse(oldLinks.get().contains(link1));
        unTrack.handle(update);
        var newLinks = userRepository.findLinksById(chatId);
        assertFalse(newLinks.get().contains(link1));
        Assertions.assertEquals(oldLinks, newLinks);
    }
}
