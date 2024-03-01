package edu.java.bot.service.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.domain.SupportedDomain;
import edu.java.bot.entity.Link;
import edu.java.bot.repository.UserRepository;
import org.springframework.stereotype.Service;
import static java.lang.String.format;

@Service
public class TrackCommand extends CommandHandler {
    private static final String LINK_HAS_STARTED_TO_BE_TRACKED = "Начала отслеживаться ссылка";
    private static final String LINK_IS_INVALID = "Домен ссылки не поддерживается";
    private static final String RESPONSE_LINK_HAS_STARTED_TO_BE_TRACKED = "Вы начали отслеживать контент по ссылке";
    private static final String RESPONSE_LINK_IS_ALREADY_BEING_TRACKED = "Вы уже отслеживаете контент этой по ссылке";
    private static final String RESPONSE_COMMAND_SUPPORTS_ONE_PARAMETER =
        "Вы должны передать 1 ссылку с этой командой";
    private static final String RESPONSE_LINK_IS_INVALID =
        "Извините, пока что я не могу работать с ссылкой этого домена";
    private final SupportedDomain supportedDomain;

    public TrackCommand(UserRepository userRepository, SupportedDomain supportedDomain, UnTrackCommand unTrackCommand) {
        super(userRepository);
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
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String[] message = update.message().text().split(" ");
        var isTheCorrectCommand = checkingThatThisIsTheCorrectCommand(this, message[0], update);
        if (isTheCorrectCommand != null) {
            return isTheCorrectCommand;
        }

        Link validLink;
        StringBuilder response = new StringBuilder();
        if (message.length != 2) {
            return new SendMessage(chatId, RESPONSE_COMMAND_SUPPORTS_ONE_PARAMETER);
        }
        if (!supportedDomain.isValid(message[1])) {
            response.append(loggingAndCreateResponse(
                LINK_IS_INVALID,
                RESPONSE_LINK_IS_INVALID,
                message[1],
                chatId
            ));
        } else {
            validLink = parsePartOfMessageIntoLink(message[1]);
            if (isDuplicateLinks(validLink, chatId)) {
                return new SendMessage(chatId, RESPONSE_LINK_IS_ALREADY_BEING_TRACKED);
            }
            userRepository.saveLink(chatId, validLink);
            response.append(loggingAndCreateResponse(
                LINK_HAS_STARTED_TO_BE_TRACKED,
                RESPONSE_LINK_HAS_STARTED_TO_BE_TRACKED,
                validLink.toString(),
                chatId
            ));
        }
        return new SendMessage(chatId, response.toString());
    }

    private StringBuilder loggingAndCreateResponse(String log, String resp, String link, Long chatId) {
        LOGGER.info(format(CHAT_ID_FOR_LOGGER, chatId)
            + format(LINK_FOR_LOGGER, link)
            + log);
        return new StringBuilder()
            .append(resp)
            .append(" (")
            .append(link)
            .append(")\n");
    }

    private boolean isDuplicateLinks(Link link, Long chatId) {
        var userLinks = userRepository.findLinksById(chatId);
        return userLinks.map(links -> links.contains(link)).orElse(true);
    }
}
