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
    private static final String RESPONSE_LINK_IS_NO_LONGER_BEING_TRACKED = "Вы перестали отслеживать контент по ссылке";
    private static final String RESPONSE_LINK_NOT_TRACKED = "Вы не отслеживали контент по ссылке";
    private static final String RESPONSE_COMMAND_SUPPORTS_ONE_PARAMETER =
        "Вы должны передать 1 ссылку с этой командой";
    private static final String RESPONSE_LINK_IS_INVALID =
        "Извините, пока что я не могу работать с ссылкой этого домена";
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
        } else {
            try {
                String link = message[1];
                scrapperService.removeLink(chatId, link);
                response = RESPONSE_LINK_IS_NO_LONGER_BEING_TRACKED + " (" + link + ")\n";
                log.info(format(CHAT_ID_FOR_LOGGER, chatId)
                    + format(LINK_FOR_LOGGER, link)
                    + LINK_IS_NO_LONGER_BEING_TRACKED);
            } catch (RuntimeException ex) {
                switch (ex) {
                    case LinkNotFoundException linkNotFoundException -> response = RESPONSE_LINK_NOT_TRACKED;
                    case ChatIdNotFoundException chatIdNotFoundException -> response = USER_IS_NOT_REGISTERED;
                    case IllegalArgumentException illegalArgumentException -> response = BAD_REQUEST;
                    default -> throw ex;
                }
            }
        }
        return new SendMessage(chatId, response);
    }
}
