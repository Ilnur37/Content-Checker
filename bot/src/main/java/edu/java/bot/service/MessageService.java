package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.command.SupportedCommand;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final SupportedCommand supportedCommand;

    public MessageService(SupportedCommand supportedCommand) {
        this.supportedCommand = supportedCommand;
    }

    public SendMessage process(Update update) {
        return supportedCommand.getCommandHandler().handle(update);
    }
}
