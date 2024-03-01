package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import java.util.List;
import java.util.Objects;
import edu.java.bot.service.handler.HelpCommand;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final HelpCommand helpCommand;

    public MessageController(TelegramBot telegramBot, HelpCommand helpCommand) {
        telegramBot.setUpdatesListener(this::process);
        this.telegramBot = telegramBot;
        this.helpCommand = helpCommand;
    }

    @Override
    public int process(List<Update> updates) {
        updates.stream()
            .filter(Objects::nonNull)
            .map(helpCommand::handle)
            .forEach(telegramBot::execute);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
