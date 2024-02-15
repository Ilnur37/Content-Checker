package edu.java.bot.service.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.Link;
import edu.java.bot.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ListCommand extends CommandHandler {
    private static final String RESPONSE_LIST_OF_TRACKED_LINKS = "Вы отслеживаете %d ссылок \n";

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
        var isTheCorrectCommand = checkingThatThisIsTheCorrectCommand(this, update.message().text(), update);
        if (isTheCorrectCommand != null) {
            return isTheCorrectCommand;
        }

        var trackedLinks = userRepository.findLinksById(chatId);
        int countLinks = 0;
        if (trackedLinks.isPresent()) {
            for (Link link : trackedLinks.get()) {
                response.append("(")
                    .append(link)
                    .append(")\n");
                countLinks++;
            }
        }
        response.insert(0, String.format(RESPONSE_LIST_OF_TRACKED_LINKS, countLinks));
        return new SendMessage(chatId, response.toString());
    }
}
