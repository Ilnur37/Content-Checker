package edu.java.bot.service.handler;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.Domain;
import edu.java.bot.entity.Link;
import edu.java.bot.repository.UserRepository;
import java.net.URI;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public abstract class CommandHandler {
    private static final String HTTP = "://";
    protected static final Logger LOGGER = LogManager.getLogger();
    protected static final String CHAT_ID_FOR_LOGGER = " [chatId = %d] ";
    protected static final String LINK_FOR_LOGGER = " [link = %s] ";
    protected static final String UNSUPPORTED_COMMAND = "Пока что я не могу распознать это сообщение";
    protected final UserRepository userRepository;

    @Getter
    //@Setter
    protected CommandHandler next;

    public CommandHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public abstract String command();

    public abstract String description();

    public abstract SendMessage handle(Update update);

    public BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }

    @SneakyThrows
    public Link parsePartOfMessageIntoLink(String message) {
        URI uri = new URI(message);
        Domain domain = new Domain(uri.getScheme() + HTTP + uri.getHost());
        return new Link(domain, uri.getPath());
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
