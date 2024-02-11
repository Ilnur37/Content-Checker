package CommandTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotApplication;
import edu.java.bot.service.handler.HelpCommand;
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
public class HelpTest {
    private static final String trueResult =
        """
            Вы можете воспользоваться следующими командами:
            /help : Вывести окно с командами
            /start : Зарегестрировать пользователя
            /track : Начать отслеживание ссылки
            /untrack : Прекратить отслеживание ссылки
            /list : Показать список отслеживаемых ссылок
            """;

    @MockBean
    Update update;
    @Autowired
    HelpCommand help;

    @Test
    @DisplayName("Корректные данные")
    void helpCommand() {
        Long chatId = new Random().nextLong();

        Message mockMessage = mock(Message.class);
        Chat mockChat =  mock(Chat.class);
        when(update.message()).thenReturn(mockMessage);
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockMessage.text()).thenReturn(help.command());
        when(mockChat.id()).thenReturn(chatId);
        SendMessage response = help.handle(update);
        Assertions.assertEquals(trueResult, response.getParameters().get("text"));
    }
}
