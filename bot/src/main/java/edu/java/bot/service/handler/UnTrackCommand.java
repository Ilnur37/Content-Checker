package edu.java.bot.service.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.Link;
import edu.java.bot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static java.lang.String.format;

@Service
public class UnTrackCommand extends CommandHandler {
    private static final String LINK_IS_NO_LONGER_BEING_TRACKED = "Ссылка перестала отслеживается";
    private static final String RESPONSE_LINK_IS_NO_LONGER_BEING_TRACKED = "Вы перестали отслеживать контент по ссылке";
    private static final String RESPONSE_LINK_NOT_TRACKED = "Вы не отслеживали контент по ссылке";
    private static final String RESPONSE_COMMAND_SUPPORTS_ONE_PARAMETER =
        "Вы можете передать только 1 ссылку с этой командой";

    @Autowired
    public UnTrackCommand(UserRepository userRepository) {
        super(userRepository);
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
        if (!message[0].equals(command())) {
            if (next != null) {
                return next.handle(update);
            }
            return new SendMessage(chatId, UNSUPPORTED_COMMAND);
        }

        Link link;
        StringBuilder response = new StringBuilder();
        if (message.length != 2) {
            response.append(RESPONSE_COMMAND_SUPPORTS_ONE_PARAMETER);
        }
        if (message.length != 1) {
            link = parsePartOfMessageIntoLink(message[1]);

            if (isUntrackedLink(link, chatId)) {
                return new SendMessage(chatId, RESPONSE_LINK_NOT_TRACKED);
            }

            userRepository.deleteLink(chatId, link);
            LOGGER.info(format(CHAT_ID_FOR_LOGGER, chatId)
                + format(LINK_FOR_LOGGER, link)
                + LINK_IS_NO_LONGER_BEING_TRACKED);
            response.append(RESPONSE_LINK_IS_NO_LONGER_BEING_TRACKED)
                .append(" (")
                .append(link)
                .append(")\n");

            if (message.length > 2) {
                response.append(RESPONSE_COMMAND_SUPPORTS_ONE_PARAMETER);
            }
        }
        return new SendMessage(chatId, response.toString());

    }

    private boolean isUntrackedLink(Link link, Long chatId) {
        var userLinks = userRepository.findLinksById(chatId);
        return userLinks.map(links -> !links.contains(link)).orElse(true);
    }
}
