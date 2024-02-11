package edu.java.bot.service.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.Link;
import edu.java.bot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListCommand extends CommandHandler {
    private static final String RESPONSE_LIST_OF_TRACKED_LINKS = "Вы отслеживаете %d ссылок \n";

    @Autowired
    public ListCommand(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        StringBuilder response = new StringBuilder();
        if (!update.message().text().equals(command())) {
            if (next != null) {
                return next.handle(update);
            }
            return new SendMessage(chatId, UNSUPPORTED_COMMAND);
        }

        var trackedLinks = userRepository.findLinksById(chatId);
        if (trackedLinks.isPresent()) {
            for (Link link : trackedLinks.get()) {
                response.append("(")
                    .append(link)
                    .append(")\n");
            }
        }
        response.insert(0, String.format(RESPONSE_LIST_OF_TRACKED_LINKS, trackedLinks.get().size()));
        return new SendMessage(chatId, response.toString());
    }
}
