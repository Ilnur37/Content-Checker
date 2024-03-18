package edu.java.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.domain.SupportedDomain;
import edu.java.bot.service.handler.TrackCommand;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.InvalidUrlException;
import edu.java.models.exception.ReAddLinkException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TrackCommandTest extends AbstractTest {

    @Autowired
    private TrackCommand trackCommand;

    @Test
    @DisplayName("Добавление 1 поддерживаемой ссылки")
    void trackLinkWhenSupportedDomain() {
        String link = "https://github.com/Ilnur37/Content-Checker";
        LinkResponse actualLinkResponse = new LinkResponse(chatId, link);
        when(scrapperService.addLink(chatId, link)).thenReturn(actualLinkResponse);
        mockObjects(chatId, trackCommand.command() + " " + link);

        SendMessage response = trackCommand.handle(update);

        assertEquals(
            toResponse(TrackCommand.RESPONSE_LINK_HAS_STARTED_TO_BE_TRACKED, link),
            response.getParameters().get("text")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://github.com/Ilnur37/Content-Checker/pull",
        "https://translate.google.com/?sl=ru&tl=en&op=translate"})
    @DisplayName("Добавление неподдерживаемой ссылки")
    void trackLinkWhenUnsupportedDomain(String link) {
        mockObjects(chatId, trackCommand.command() + " " + link);

        SendMessage response = trackCommand.handle(update);

        assertEquals(
            SupportedDomain.RESPONSE_LINK_IS_INVALID,
            response.getParameters().get("text")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"",
        " url url"})
    void trackWhenBadCommandTrack(String link) {
        mockObjects(chatId, trackCommand.command() + link);

        SendMessage response = trackCommand.handle(update);

        assertEquals(
            SupportedDomain.RESPONSE_COMMAND_SUPPORTS_ONE_PARAMETER,
            response.getParameters().get("text")
        );
    }

    @Test
    @DisplayName("Повторное добавление ссылки")
    void trackLinkWhenRepeatSupportedLinks() {
        String link = "https://github.com/Ilnur37/Content-Checker";
        Mockito.doThrow(new ReAddLinkException("")).when(scrapperService).addLink(chatId, link);
        mockObjects(chatId, trackCommand.command() + " " + link);

        SendMessage response = trackCommand.handle(update);

        assertEquals(
            TrackCommand.RESPONSE_LINK_IS_ALREADY_BEING_TRACKED,
            response.getParameters().get("text")
        );
    }

    @Test
    @DisplayName("Добавление несуществующей ссылки")
    void trackLinkWhenLinkNotExist() {
        String link = "https://github.com/Ilnur37/Content-Checker";
        Mockito.doThrow(new InvalidUrlException("")).when(scrapperService).addLink(chatId, link);
        mockObjects(chatId, trackCommand.command() + " " + link);

        SendMessage response = trackCommand.handle(update);

        assertEquals(
            TrackCommand.RESPONSE_URL_IS_EMPTY,
            response.getParameters().get("text")
        );
    }

    @Test
    @DisplayName("Пользователь не зарегистрирован")
    void trackLinkWhenUserNotRegistered() {
        String link = "https://github.com/Ilnur37/Content-Checker";
        Mockito.doThrow(new ChatIdNotFoundException("")).when(scrapperService).addLink(chatId, link);
        mockObjects(chatId, trackCommand.command() + " " + link);

        SendMessage response = trackCommand.handle(update);

        assertEquals(
            TrackCommand.USER_IS_NOT_REGISTERED,
            response.getParameters().get("text")
        );
    }

    @Test
    void trackLinkWhenBadRequest() {
        String link = "https://github.com/Ilnur37/Content-Checker";
        Mockito.doThrow(new IllegalArgumentException("")).when(scrapperService).addLink(chatId, link);
        mockObjects(chatId, trackCommand.command() + " " + link);

        SendMessage response = trackCommand.handle(update);

        assertEquals(
            TrackCommand.BAD_REQUEST,
            response.getParameters().get("text")
        );
    }
}
