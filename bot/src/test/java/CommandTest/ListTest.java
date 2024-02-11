package CommandTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotApplication;
import edu.java.bot.entity.Domain;
import edu.java.bot.entity.Link;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.handler.ListCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Random;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BotApplication.class})
public class ListTest {
    private static final String RESPONSE_LIST_OF_TRACKED_LINKS = "Вы отслеживаете %d ссылок \n";

    @MockBean
    Update update;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ListCommand list;

    private void mockObjects(Long id) {
        userRepository.saveUser(new User(id));
        Message mockMessage = mock(Message.class);
        Chat mockChat = mock(Chat.class);
        when(update.message()).thenReturn(mockMessage);
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockMessage.text()).thenReturn(list.command());
        when(mockChat.id()).thenReturn(id);
    }

    @Test
    @DisplayName("Показать список из N отслеживаемых ссылок")
    void listOfLinks_whenNtrackingLinks() {
        Long chatId = new Random().nextLong();
        Link link1 = new Link(new Domain("https://github.com"), "/Ilnur37/Content-Checker");
        Link link2 = new Link(new Domain("https://github.com"), "/Ilnur37/Content-Checker2");
        String trueResult = String.format(RESPONSE_LIST_OF_TRACKED_LINKS, 2)
            + "(https://github.com/Ilnur37/Content-Checker)\n"
            + "(https://github.com/Ilnur37/Content-Checker2)\n";
        userRepository.saveUser(new User(chatId));
        userRepository.saveLink(chatId, link1);
        userRepository.saveLink(chatId, link2);
        mockObjects(chatId);
        SendMessage message = list.handle(update);
        Assertions.assertEquals(message.getParameters().get("text"), trueResult);
    }

    @Test
    @DisplayName("Показать список из 0 отслеживаемых ссылок")
    void listOfLinks_when0trackingLinks() {
        Long chatId = new Random().nextLong();
        String trueResult = String.format(RESPONSE_LIST_OF_TRACKED_LINKS, 0);
        userRepository.saveUser(new User(chatId));
        mockObjects(chatId);
        SendMessage message = list.handle(update);
        Assertions.assertEquals(message.getParameters().get("text"), trueResult);
    }
}
