package edu.java.bot.service.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.ScrapperService;
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
        "Вы можете передать только 1 ссылку с этой командой";

    public UnTrackCommand(ScrapperService scrapperService, ListCommand listCommand) {
        super(scrapperService);
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
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String[] message = update.message().text().split(" ");
        var isTheCorrectCommand = checkingThatThisIsTheCorrectCommand(this, message[0], update);
        if (isTheCorrectCommand != null) {
            return isTheCorrectCommand;
        }
        if (message.length != 2) {
            return new SendMessage(chatId, RESPONSE_COMMAND_SUPPORTS_ONE_PARAMETER);
        }
        String link = message[1];
        StringBuilder response = new StringBuilder();
        try {
            scrapperService.removeLink(chatId, link);
            log.info(format(CHAT_ID_FOR_LOGGER, chatId)
                + format(LINK_FOR_LOGGER, link)
                + LINK_IS_NO_LONGER_BEING_TRACKED);
            response.append(toResponse(RESPONSE_LINK_IS_NO_LONGER_BEING_TRACKED, link));
        } catch (RuntimeException ex) {
            switch (ex.getMessage()) {
                case BAD_REQUEST_HTTP:
                    response.append(BAD_REQUEST);
                    break;
                case NOT_FOUND_HTTP:
                    response.append(RESPONSE_LINK_NOT_TRACKED);
                    break;
                default:
                    throw ex;
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
