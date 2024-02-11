package CommandTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.entity.Domain;
import edu.java.bot.entity.Link;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.handler.UnTrackCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BotApplication.class})
public class UnTrackTest {
    @MockBean
    Update update;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UnTrackCommand unTrack;

    private void mockObjects(Long id, String command) {
        userRepository.saveUser(new User(id));
        userRepository.saveLink(id, new Link(new Domain("https://github.com"), "/Ilnur37/Content-Checker"));
        Message mockMessage = mock(Message.class);
        Chat mockChat = mock(Chat.class);
        when(update.message()).thenReturn(mockMessage);
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockMessage.text()).thenReturn(command);
        when(mockChat.id()).thenReturn(id);
    }

    @Test
    @DisplayName("Удаление 1 отслеживаемой ссылки")
    void unTrackLink_whenContainLink() {
        Long chatId = new Random().nextLong();
        String link = "https://github.com/Ilnur37/Content-Checker";
        Link link1 = new Link(new Domain("https://github.com"), "/Ilnur37/Content-Checker");

        mockObjects(chatId, unTrack.command() + " " + link);
        Assertions.assertTrue(userRepository.findLinksById(chatId).get().contains(link1));
        unTrack.handle(update);
        Assertions.assertFalse(userRepository.findLinksById(chatId).get().contains(link1));
    }

    @Test
    @DisplayName("Удаление неотслеживаемой ссылки")
    void unTrackLink_whenNoContainLink() {
        Long chatId = new Random().nextLong();
        String link = "https://github.com/Ilnur37/Content-Checker";
        Link link1 = new Link(new Domain("https://github.com"), "/Ilnur37");

        mockObjects(chatId, unTrack.command() + " " + link);
        var oldLinks = userRepository.findLinksById(chatId);
        Assertions.assertFalse(oldLinks.get().contains(link1));
        unTrack.handle(update);
        var newLinks = userRepository.findLinksById(chatId);
        Assertions.assertFalse(newLinks.get().contains(link1));
        Assertions.assertEquals(oldLinks, newLinks);
    }
}
