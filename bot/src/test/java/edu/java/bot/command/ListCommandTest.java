package edu.java.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.handler.ListCommand;
import edu.java.models.exception.ChatIdNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListCommandTest extends AbstractTest {
    @Autowired
    private ListCommand listCommand;

    @Test
    @DisplayName("Пользователь не зарегистрирован")
    void trackLinkWhenUserNotRegistered() {
        Mockito.doThrow(new ChatIdNotFoundException("")).when(scrapperService).getAllLinks(chatId);
        mockObjects(chatId, listCommand.command());

        SendMessage response = listCommand.handle(update);

        assertEquals(
            ListCommand.USER_IS_NOT_REGISTERED,
            response.getParameters().get("text")
        );
    }

    @Test
    void trackLinkWhenBadRequest() {
        Mockito.doThrow(new IllegalArgumentException("")).when(scrapperService).getAllLinks(chatId);
        mockObjects(chatId, listCommand.command());

        SendMessage response = listCommand.handle(update);

        assertEquals(
            ListCommand.BAD_REQUEST,
            response.getParameters().get("text")
        );
    }
}
