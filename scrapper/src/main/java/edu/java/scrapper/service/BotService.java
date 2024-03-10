package edu.java.scrapper.service;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.dao.ChatDao;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {
    private static final String NOT_FOUND_HTTP = "404 NOT_FOUND";
    private final BotClient botClient;
    private final ChatDao chatDao;

    public void sendUpdate(long id, String url, String description, List<Long> tgChatIds) {
        try {
            botClient.sendUpdate(id, url, description, tgChatIds);
        } catch (RuntimeException ex) {
            String msg = ex.getMessage();
            if (msg.contains(NOT_FOUND_HTTP)) {
                String[] parts = msg.split("\\s+");
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
            } else {
                throw ex;
            }
        }
    }
}
