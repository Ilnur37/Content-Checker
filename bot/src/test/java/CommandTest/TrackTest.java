package CommandTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.entity.Domain;
import edu.java.bot.entity.Link;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.handler.TrackCommand;
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
public class TrackTest {
    @MockBean
    Update update;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TrackCommand track;

    private void mockObjects(Long id, String command) {
        userRepository.saveUser(new User(id));
        Message mockMessage = mock(Message.class);
        Chat mockChat = mock(Chat.class);
        when(update.message()).thenReturn(mockMessage);
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockMessage.text()).thenReturn(command);
        when(mockChat.id()).thenReturn(id);
    }

    @Test
    @DisplayName("Добавление 1 поддерживаемой ссылки")
    void trackLink_whenSupportedDomain() {
        Long chatId = new Random().nextLong();
        String link = "https://github.com/Ilnur37/Content-Checker";

        mockObjects(chatId, track.command() + " " + link);
        track.handle(update);

        var newLinks = userRepository.findLinksById(chatId);
        assertThat(newLinks).isPresent();
        Assertions.assertAll(
            () -> assertThat(newLinks.get().size()).isEqualTo(1),
            () -> Assertions.assertTrue(
                newLinks.get().contains(new Link(new Domain("https://github.com"), "/Ilnur37/Content-Checker")))
        );
    }

    @Test
    @DisplayName("Добавление неподдерживаемой ссылки")
    void trackLink_whenUnsupportedDomain() {
        Long chatId = new Random().nextLong();
        String link = "https://translate.google.com/?sl=ru&tl=en&op=translate";

        mockObjects(chatId, track.command() + " " + link);
        track.handle(update);

        var newLinks = userRepository.findLinksById(chatId);
        assertThat(newLinks).isPresent();
        Assertions.assertAll(
            () -> assertThat(newLinks.get().size()).isEqualTo(0),
            () -> Assertions.assertFalse(
                newLinks.get()
                    .contains(new Link(new Domain("https://translate.google.com"), "/?sl=ru&tl=en&op=translate")))
        );
    }

    @Test
    @DisplayName("Добавление 2 поддерживаемых ссылок")
    void trackLink_2SupportedLinks() {
        Long chatId = new Random().nextLong();
        String link1 = "https://github.com/Ilnur37/Content-Checker";
        String link2 = "https://github.com/Ilnur37/Tinkoff_Course";

        mockObjects(chatId, track.command() + " " + link1 + " " + link2);
        track.handle(update);

        var newLinks = userRepository.findLinksById(chatId);
        assertThat(newLinks).isPresent();
        Assertions.assertAll(
            () -> assertThat(newLinks.get().size()).isEqualTo(1),
            () -> Assertions.assertTrue(
                newLinks.get().contains(new Link(new Domain("https://github.com"), "/Ilnur37/Content-Checker"))),
            () -> Assertions.assertFalse(
                newLinks.get().contains(new Link(new Domain("https://github.com"), "/Ilnur37/Tinkoff_Course")))
        );
    }
    @Test
    @DisplayName("Повторное добавление поддерживаемой ссылки")
    void trackLink_repeatSupportedLinks() {
        Long chatId = new Random().nextLong();
        String link = "https://github.com/Ilnur37/Content-Checker";

        mockObjects(chatId, track.command() + " " + link);
        track.handle(update);

        var newLinks = userRepository.findLinksById(chatId);
        assertThat(newLinks).isPresent();
        Assertions.assertAll(
            () -> assertThat(newLinks.get().size()).isEqualTo(1),
            () -> Assertions.assertTrue(
                newLinks.get().contains(new Link(new Domain("https://github.com"), "/Ilnur37/Content-Checker")))
        );
    }
}
