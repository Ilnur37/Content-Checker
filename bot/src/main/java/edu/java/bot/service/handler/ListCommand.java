package edu.java.bot.service.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.ScrapperService;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ListCommand extends CommandHandler {
    public static final String RESPONSE_LIST_OF_TRACKED_LINKS = "Вы отслеживаете %d ссылок \n";

    public ListCommand(ScrapperService scrapperService) {
        super(scrapperService);
        this.next = null;
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
        var isTheCorrectCommand = checkingThatThisIsTheCorrectCommand(this, update.message().text(), update);
        if (isTheCorrectCommand != null) {
            return isTheCorrectCommand;
        }

        StringBuilder response = new StringBuilder();
        try {
            ListLinksResponse links = scrapperService.getAllLinks(chatId);
            response.append(String.format(RESPONSE_LIST_OF_TRACKED_LINKS, links.size()));
            for (LinkResponse link : links.links()) {
                response.append("\n(")
                    .append(link)
                    .append(")");
            }
        } catch (ChatIdNotFoundException ex) {
            response.append(USER_IS_NOT_REGISTERED);
        } catch (IllegalArgumentException ex) {
            response.append(BAD_REQUEST);
        }
        return new SendMessage(chatId, response.toString());
    }
}
