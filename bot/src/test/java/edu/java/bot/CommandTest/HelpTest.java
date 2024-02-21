package edu.java.bot.CommandTest;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.handler.HelpCommand;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HelpTest extends AbstractTest {
    private static final String trueResult =
        """
            Вы можете воспользоваться следующими командами:
            /help : Вывести окно с командами
            /start : Зарегестрировать пользователя
            /track : Начать отслеживание ссылки
            /untrack : Прекратить отслеживание ссылки
            /list : Показать список отслеживаемых ссылок
            """;

    @Autowired
    HelpCommand help;

    @Test
    @DisplayName("Корректные данные")
    void helpCommand() {
        Long chatId = new Random().nextLong();
        mockObjects(chatId, help.command());
        SendMessage response = help.handle(update);
        Assertions.assertEquals(trueResult, response.getParameters().get("text"));
    }
}
