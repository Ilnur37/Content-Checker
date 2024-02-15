package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.MessageService;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final MessageService messageService;

    public MessageController(TelegramBot telegramBot, MessageService messageService) {
        telegramBot.setUpdatesListener(this::process);
        this.telegramBot = telegramBot;
        this.messageService = messageService;
    }

    @Override
    public int process(List<Update> updates) {
        updates.stream()
            .filter(Objects::nonNull)
            .map(messageService::process)
            .forEach(telegramBot::execute);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
