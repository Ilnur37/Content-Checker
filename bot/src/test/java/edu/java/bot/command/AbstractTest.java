package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.service.ScrapperService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BotApplication.class})
public abstract class AbstractTest {
    @MockBean
    Update update;
    @MockBean
    ScrapperService scrapperService;
    final long chatId = 10L;
    final String USER_IS_NOT_REGISTERED =
        "Прежде чем пользоваться функциями бота, вам необходимо зарегистрироваться. Введите команду \"/start\"";
    final String BAD_REQUEST = "Некорректные параметры запроса";

    void mockObjects(Long id, String command) {
        Message mockMessage = mock(Message.class);
        Chat mockChat = mock(Chat.class);
        when(update.message()).thenReturn(mockMessage);
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockMessage.text()).thenReturn(command);
        when(mockChat.id()).thenReturn(id);
    }

    String toResponse(String resp, String link) {
        return resp + " (" + link + ")\n";
    }
}
