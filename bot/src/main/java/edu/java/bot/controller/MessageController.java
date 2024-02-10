package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.MessageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final MessageService messageService;

    @Autowired
    public MessageController(TelegramBot telegramBot, MessageService messageService) {
        telegramBot.setUpdatesListener(this::process);
        this.telegramBot = telegramBot;
        this.messageService = messageService;
    }

    @Override
    public int process(List<Update> updates) {
        if (!updates.isEmpty()) {
            for (Update update : updates) {
                if (update == null) {
                    continue;
                }

                SendMessage sendMessage = messageService.process(update);
                telegramBot.execute(sendMessage);
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
