package edu.java.bot.service.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.ScrapperService;
import edu.java.models.exception.ReRegistrationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static java.lang.String.format;

@Slf4j
@Service
public class StartCommand extends CommandHandler {
    private static final String USER_SUCCESSFULLY_REGISTERED = "Пользователь успешно зарегистрирован";
    public static final String RESPONSE_USER_SUCCESSFULLY_REGISTERED = "Поздравляю, регистрация прошла успешно!";
    public static final String RESPONSE_USER_IS_ALREADY_REGISTERED = "Вы уже зарегистрированы";

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

        String response;
        try {
            scrapperService.registerChat(chatId);
            log.info(format(CHAT_ID_FOR_LOGGER, chatId) + USER_SUCCESSFULLY_REGISTERED);
            response = RESPONSE_USER_SUCCESSFULLY_REGISTERED;
        } catch (ReRegistrationException ex) {
            response = RESPONSE_USER_IS_ALREADY_REGISTERED;
        } catch (IllegalArgumentException ex) {
            response = BAD_REQUEST;
        }
        return new SendMessage(chatId, response);
    }
}
