package edu.java.bot.service.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.Link;
import edu.java.bot.repository.UserRepository;
import org.springframework.stereotype.Service;
import static java.lang.String.format;

@Service
public class UnTrackCommand extends CommandHandler {
    private static final String LINK_IS_NO_LONGER_BEING_TRACKED = "Ссылка перестала отслеживается";
    private static final String RESPONSE_LINK_IS_NO_LONGER_BEING_TRACKED = "Вы перестали отслеживать контент по ссылке";
    private static final String RESPONSE_LINK_NOT_TRACKED = "Вы не отслеживали контент по ссылке";
    private static final String RESPONSE_COMMAND_SUPPORTS_ONE_PARAMETER =
        "Вы можете передать только 1 ссылку с этой командой";

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
        var isTheCorrectCommand = checkingThatThisIsTheCorrectCommand(this, message[0], update);
        if (isTheCorrectCommand != null) {
            return isTheCorrectCommand;
        }
        if (message.length != 2) {
            return new SendMessage(chatId, RESPONSE_COMMAND_SUPPORTS_ONE_PARAMETER);
        }

        StringBuilder response = new StringBuilder();
        Link link = parsePartOfMessageIntoLink(message[1]);
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

        return new SendMessage(chatId, response.toString());
    }

    private boolean isUntrackedLink(Link link, Long chatId) {
        var userLinks = userRepository.findLinksById(chatId);
        return userLinks.map(links -> !links.contains(link)).orElse(true);
    }
}
