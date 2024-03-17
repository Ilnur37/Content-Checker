package edu.java.bot.service.handler;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.ScrapperService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class CommandHandler {
    protected static final String CHAT_ID_FOR_LOGGER = "[chatId = %d] ";
    protected static final String LINK_FOR_LOGGER = "[link = %s] ";
    protected static final String UNSUPPORTED_COMMAND = "Пока что я не могу распознать это сообщение";
    public static final String BAD_REQUEST = "Некорректные параметры запроса";
    public static final String USER_IS_NOT_REGISTERED =
        "Прежде чем пользоваться функциями бота, вам необходимо зарегистрироваться. Введите команду \"/start\"";

    protected final ScrapperService scrapperService;

    @Getter
    protected CommandHandler next;

    public abstract String command();

    public abstract String description();

    public abstract SendMessage handle(Update update);

    public BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }

    public SendMessage checkingThatThisIsTheCorrectCommand(
        CommandHandler currCommandHandler,
        String updateCommand,
        Update update
    ) {
        if (!updateCommand.equals(currCommandHandler.command())) {
            if (currCommandHandler.next != null) {
                return currCommandHandler.next.handle(update);
            }
            return new SendMessage(update.message().chat().id(), UNSUPPORTED_COMMAND);
        }
        return null;
    }
}
