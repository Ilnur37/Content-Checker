package edu.java.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.handler.StartCommand;
import edu.java.models.exception.ReRegistrationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

public class StartCommandTest extends AbstractTest {

    @Autowired
    StartCommand start;

    @Test
    @DisplayName("Корректные данные")
    void registrationUserWhenValidData() {
        doNothing().when(scrapperService).registerChat(chatId);
        mockObjects(chatId, start.command());
        SendMessage response = start.handle(update);

        assertEquals(response.getParameters().get("text"), StartCommand.RESPONSE_USER_SUCCESSFULLY_REGISTERED);
    }

    @Test
    @DisplayName("Повторная регистрация")
    void registrationUserWhenRepeatChatId() {
        Mockito.doThrow(new ReRegistrationException("")).when(scrapperService).registerChat(chatId);
        mockObjects(chatId, start.command());
        SendMessage response = start.handle(update);

        assertEquals(response.getParameters().get("text"), StartCommand.RESPONSE_USER_IS_ALREADY_REGISTERED);
    }
}
