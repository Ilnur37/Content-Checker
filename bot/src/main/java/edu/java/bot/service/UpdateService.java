package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.models.dto.request.LinkUpdateRequest;
import edu.java.models.exception.ChatIdNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UpdateService {
    private static final String DESCRIPTION = "Доступно новое обновление по ссылке ";
    private final TelegramBot telegramBot;

    public UpdateService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendUpdate(LinkUpdateRequest linkUpdateRequest) {
        List<SendMessage> messages = linkUpdateRequest.tgChatIds()
            .stream()
            .map(tgChatId -> new SendMessage(
                tgChatId,
                DESCRIPTION + linkUpdateRequest.url() + "\n\n" + linkUpdateRequest.description()
            ))
            .toList();
        List<Long> badIds = new ArrayList<>();

        for (SendMessage message : messages) {
            try {
                telegramBot.execute(message);
            } catch (RuntimeException ex) {
                badIds.add((long) message.getParameters().get("chat_id"));
            }
        }

        if (!badIds.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            badIds.forEach(id -> msg.append(id).append(" "));
            throw new ChatIdNotFoundException(msg.toString());
        }
    }
}
