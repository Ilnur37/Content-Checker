package edu.java.bot.service.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.ScrapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static java.lang.String.format;

@Slf4j
@Service
public class StartCommand extends CommandHandler {
    private static final String USER_SUCCESSFULLY_REGISTERED = "Пользователь успешно зарестрирован";
    private static final String RESPONSE_USER_SUCCESSFULLY_REGISTERED = "Поздравляю, регистрация прошла успешно!";
    private static final String RESPONSE_USER_IS_ALREADY_REGISTERED = "Вы уже зарагестрированны";

    public StartCommand(ScrapperService scrapperService, TrackCommand trackCommand) {
        super(scrapperService);
        this.next = trackCommand;
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Зарегестрировать пользователя";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();

        var isTheCorrectCommand = checkingThatThisIsTheCorrectCommand(this, update.message().text(), update);
        if (isTheCorrectCommand != null) {
            return isTheCorrectCommand;
        }

        StringBuilder response = new StringBuilder();
        try {
            scrapperService.registerChat(chatId);
            log.info(format(CHAT_ID_FOR_LOGGER, chatId) + USER_SUCCESSFULLY_REGISTERED);
            response.append(RESPONSE_USER_SUCCESSFULLY_REGISTERED);
        } catch (RuntimeException ex) {
            switch (ex.getMessage()) {
                case BAD_REQUEST_HTTP:
                    response.append(BAD_REQUEST);
                    break;
                case CONFLICT_HTTP:
                    response.append(RESPONSE_USER_IS_ALREADY_REGISTERED);
                    break;
                default:
                    throw ex;
            }
        }
        return new SendMessage(chatId, response.toString());
    }
}
