package CommandTest;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.Domain;
import edu.java.bot.entity.Link;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.handler.ListCommand;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListTest extends AbstractTest {
    private static final String RESPONSE_LIST_OF_TRACKED_LINKS = "Вы отслеживаете %d ссылок \n";

    @Autowired
    UserRepository userRepository;
    @Autowired
    ListCommand list;

    @Override
    protected void mockObjects(Long id, String command) {
        userRepository.saveUser(new User(id));
        super.mockObjects(id, command);
    }

    @Test
    @DisplayName("Показать список из N отслеживаемых ссылок")
    void listOfLinksWhenNtrackingLinks() {
        Long chatId = new Random().nextLong();
        Link link1 = new Link(new Domain("https://github.com"), "/Ilnur37/Content-Checker");
        Link link2 = new Link(new Domain("https://github.com"), "/Ilnur37/Content-Checker2");
        String expectedResult = String.format(RESPONSE_LIST_OF_TRACKED_LINKS, 2)
            + "(https://github.com/Ilnur37/Content-Checker)\n"
            + "(https://github.com/Ilnur37/Content-Checker2)\n";
        userRepository.saveUser(new User(chatId));
        userRepository.saveLink(chatId, link1);
        userRepository.saveLink(chatId, link2);
        mockObjects(chatId, list.command());
        SendMessage message = list.handle(update);
        assertEquals(message.getParameters().get("text"), expectedResult);
    }

    @Test
    @DisplayName("Показать список из 0 отслеживаемых ссылок")
    void listOfLinksWhen0trackingLinks() {
        Long chatId = new Random().nextLong();
        String expectedResult = String.format(RESPONSE_LIST_OF_TRACKED_LINKS, 0);
        userRepository.saveUser(new User(chatId));
        mockObjects(chatId, list.command());
        SendMessage message = list.handle(update);
        assertEquals(message.getParameters().get("text"), expectedResult);
    }
}
