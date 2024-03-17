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
    public static final String RESPONSE_LINK_HAS_STARTED_TO_BE_TRACKED = "Вы начали отслеживать контент по ссылке";
    public static final String RESPONSE_LINK_IS_ALREADY_BEING_TRACKED = "Вы уже отслеживаете контент этой по ссылке";

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
    @SuppressWarnings("InnerAssignment")
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String[] message = update.message().text().split(" ");
        var isTheCorrectCommand = checkingThatThisIsTheCorrectCommand(this, message[0], update);
        if (isTheCorrectCommand != null) {
            return isTheCorrectCommand;
        }

        String response = supportedDomain.validateCommand(message);
        if (!response.isEmpty()) {
            return new SendMessage(chatId, response);
        } else {
            try {
                String link = message[1];
                scrapperService.addLink(chatId, link);
                response = RESPONSE_LINK_HAS_STARTED_TO_BE_TRACKED + " (" + link + ")\n";
                log.info(format(CHAT_ID_FOR_LOGGER, chatId)
                    + format(LINK_FOR_LOGGER, link)
                    + LINK_HAS_STARTED_TO_BE_TRACKED);
            } catch (ReAddLinkException ex) {
                response = RESPONSE_LINK_IS_ALREADY_BEING_TRACKED;
            } catch (ChatIdNotFoundException ex) {
                response = USER_IS_NOT_REGISTERED;
            } catch (IllegalArgumentException ex) {
                response = BAD_REQUEST;
            }
        }
        return new SendMessage(chatId, response);
    }
}
