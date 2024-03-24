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
    StartCommand startCommand;

    @Test
    @DisplayName("Корректные данные")
    void registrationUserWhenValidData() {
        doNothing().when(scrapperService).registerChat(chatId);
        mockObjects(chatId, startCommand.command());

        SendMessage response = startCommand.handle(update);

        assertEquals(StartCommand.RESPONSE_USER_SUCCESSFULLY_REGISTERED, response.getParameters().get("text"));
    }

    @Test
    @DisplayName("Повторная регистрация")
    void registrationUserWhenRepeatChatId() {
        Mockito.doThrow(new ReRegistrationException("")).when(scrapperService).registerChat(chatId);
        mockObjects(chatId, startCommand.command());

        SendMessage response = startCommand.handle(update);

        assertEquals(StartCommand.RESPONSE_USER_IS_ALREADY_REGISTERED, response.getParameters().get("text"));
    }
}
