package edu.java.scrapper.service;

import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.domain.ChatDao;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotService {
    private final BotClient botClient;
    private final ChatDao chatDao;

    public void sendUpdate(long id, String url, String description, List<Long> tgChatIds) {
        try {
            botClient.sendUpdate(id, url, description, tgChatIds);
        } catch (ChatIdNotFoundException ex) {
            deleteNonExtentIds(ex.getMessage());
        } catch (IllegalArgumentException ignore) {
        }
    }

    private void deleteNonExtentIds(String message) {
        String[] parts = message.split("\\s+");
        List<Long> badIds = new ArrayList<>();
        // Пропуск первых двух элементов и извлечение чисел
        for (int i = 2; i < parts.length; i++) {
            try {
                long number = Long.parseLong(parts[i]);
                badIds.add(number);
            } catch (NumberFormatException ignored) {
            }
        }
        badIds.forEach(chatDao::delete);
    }
}
