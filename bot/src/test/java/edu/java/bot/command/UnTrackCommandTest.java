package edu.java.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.domain.SupportedDomain;
import edu.java.bot.service.handler.UnTrackCommand;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.LinkNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UnTrackCommandTest extends AbstractTest {

    @Autowired
    UnTrackCommand unTrackCommand;

    @Test
    @DisplayName("Удаление 1 поддерживаемой ссылки")
    void unTrackLinkWhenContainLink() {
        String link = "https://github.com/Ilnur37/Content-Checker";
        LinkResponse actualLinkResponse = new LinkResponse(chatId, link);
        when(scrapperService.removeLink(chatId, link)).thenReturn(actualLinkResponse);
        mockObjects(chatId, unTrackCommand.command() + " " + link);

        SendMessage response = unTrackCommand.handle(update);

        assertEquals(
            toResponse(UnTrackCommand.RESPONSE_LINK_IS_NO_LONGER_BEING_TRACKED, link),
            response.getParameters().get("text")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://github.com/Ilnur37/Content-Checker/pull",
        "https://translate.google.com/?sl=ru&tl=en&op=translate"})
    @DisplayName("Удаление неподдерживаемой ссылки")
    void unTrackLinkWhenUnsupportedDomain(String link) {
        mockObjects(chatId, unTrackCommand.command() + " " + link);

        SendMessage response = unTrackCommand.handle(update);

        assertEquals(
            SupportedDomain.RESPONSE_LINK_IS_INVALID,
            response.getParameters().get("text")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"",
        " url url"})
    void unTrackWhenBadCommandTrack(String link) {
        mockObjects(chatId, unTrackCommand.command() + link);

        SendMessage response = unTrackCommand.handle(update);

        assertEquals(
            SupportedDomain.RESPONSE_COMMAND_SUPPORTS_ONE_PARAMETER,
            response.getParameters().get("text")
        );
    }

    @Test
    @DisplayName("Удаление несуществующей ссылки")
    void unTrackLinkWhenRepeatSupportedLinks() {
        String link = "https://github.com/Ilnur37/Content-Checker";
        Mockito.doThrow(new LinkNotFoundException("")).when(scrapperService).removeLink(chatId, link);
        mockObjects(chatId, unTrackCommand.command() + " " + link);

        SendMessage response = unTrackCommand.handle(update);

        assertEquals(
            UnTrackCommand.RESPONSE_LINK_NOT_TRACKED,
            response.getParameters().get("text")
        );
    }

    @Test
    @DisplayName("Пользователь не зарегистрирован")
    void unTrackLinkWhenUserNotRegistered() {
        String link = "https://github.com/Ilnur37/Content-Checker";
        Mockito.doThrow(new ChatIdNotFoundException("")).when(scrapperService).removeLink(chatId, link);
        mockObjects(chatId, unTrackCommand.command() + " " + link);

        SendMessage response = unTrackCommand.handle(update);

        assertEquals(
            UnTrackCommand.USER_IS_NOT_REGISTERED,
            response.getParameters().get("text")
        );
    }

    @Test
    void unTrackLinkWhenBadRequest() {
        String link = "https://github.com/Ilnur37/Content-Checker";
        Mockito.doThrow(new IllegalArgumentException("")).when(scrapperService).removeLink(chatId, link);
        mockObjects(chatId, unTrackCommand.command() + " " + link);

        SendMessage response = unTrackCommand.handle(update);

        assertEquals(
            UnTrackCommand.BAD_REQUEST,
            response.getParameters().get("text")
        );
    }
}
