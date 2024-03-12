package edu.java.bot.service.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.domain.SupportedDomain;
import edu.java.bot.service.ScrapperService;
import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.ReAddLinkException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static java.lang.String.format;

@Slf4j
@Service
public class TrackCommand extends CommandHandler {
    private static final String LINK_HAS_STARTED_TO_BE_TRACKED = "Начала отслеживаться ссылка";
    private static final String RESPONSE_LINK_HAS_STARTED_TO_BE_TRACKED = "Вы начали отслеживать контент по ссылке";
    private static final String RESPONSE_LINK_IS_ALREADY_BEING_TRACKED = "Вы уже отслеживаете контент этой по ссылке";
    private static final String RESPONSE_COMMAND_SUPPORTS_ONE_PARAMETER =
        "Вы должны передать 1 ссылку с этой командой";
    private static final String RESPONSE_LINK_IS_INVALID =
        "Извините, пока что я не могу работать с ссылкой этого домена";
    private final SupportedDomain supportedDomain;

    public TrackCommand(
        ScrapperService scrapperService,
        SupportedDomain supportedDomain,
        UnTrackCommand unTrackCommand
    ) {
        super(scrapperService);
        this.supportedDomain = supportedDomain;
        this.next = unTrackCommand;
    }

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Начать отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String[] message = update.message().text().split(" ");
        var isTheCorrectCommand = checkingThatThisIsTheCorrectCommand(this, message[0], update);
        if (isTheCorrectCommand != null) {
            return isTheCorrectCommand;
        }

        StringBuilder response = new StringBuilder();
        if (message.length != 2) {
            return new SendMessage(chatId, RESPONSE_COMMAND_SUPPORTS_ONE_PARAMETER);
        }
        String link = message[1];
        if (!supportedDomain.isValid(link)) {
            response.append(toResponse(RESPONSE_LINK_IS_INVALID, link));
        } else {
            try {
                scrapperService.addLink(chatId, link);
                response.append(toResponse(RESPONSE_LINK_HAS_STARTED_TO_BE_TRACKED, link));
                log.info(format(CHAT_ID_FOR_LOGGER, chatId)
                    + format(LINK_FOR_LOGGER, link)
                    + LINK_HAS_STARTED_TO_BE_TRACKED);
            } catch (RuntimeException ex) {
                switch (ex) {
                    case ReAddLinkException reAddLinkException ->
                        response.append(RESPONSE_LINK_IS_ALREADY_BEING_TRACKED);
                    case ChatIdNotFoundException chatIdNotFoundException -> response.append(USER_IS_NOT_REGISTERED);
                    case IllegalArgumentException illegalArgumentException -> response.append(BAD_REQUEST);
                    default -> throw ex;
                }
            }
        }
        return new SendMessage(chatId, response.toString());
    }

    private StringBuilder toResponse(String resp, String link) {
        return new StringBuilder()
            .append(resp)
            .append(" (")
            .append(link)
            .append(")\n");
    }
}
