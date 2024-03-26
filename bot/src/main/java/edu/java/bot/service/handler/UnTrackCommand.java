package edu.java.bot.service.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.domain.SupportedDomain;
import edu.java.bot.service.ScrapperService;
import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.LinkNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static java.lang.String.format;

@Slf4j
@Service
public class UnTrackCommand extends CommandHandler {
    private static final String LINK_IS_NO_LONGER_BEING_TRACKED = "Ссылка перестала отслеживается";
    public static final String RESPONSE_LINK_IS_NO_LONGER_BEING_TRACKED = "Вы перестали отслеживать контент по ссылке";
    public static final String RESPONSE_LINK_NOT_TRACKED = "Вы не отслеживали контент по ссылке";

    private final SupportedDomain supportedDomain;

    public UnTrackCommand(
        ScrapperService scrapperService,
        SupportedDomain supportedDomain,
        ListCommand listCommand
    ) {
        super(scrapperService);
        this.supportedDomain = supportedDomain;
        this.next = listCommand;
    }

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Прекратить отслеживание ссылки";
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
        }
        try {
            String link = message[1];
            scrapperService.removeLink(chatId, link);
            response = RESPONSE_LINK_IS_NO_LONGER_BEING_TRACKED + " (" + link + ")\n";
            log.info(format(CHAT_ID_FOR_LOGGER, chatId)
                + format(LINK_FOR_LOGGER, link)
                + LINK_IS_NO_LONGER_BEING_TRACKED);
        } catch (LinkNotFoundException ex) {
            response = RESPONSE_LINK_NOT_TRACKED;
        } catch (ChatIdNotFoundException ex) {
            response = USER_IS_NOT_REGISTERED;
        } catch (IllegalArgumentException ex) {
            response = BAD_REQUEST;
        }

        return new SendMessage(chatId, response);
    }
}
