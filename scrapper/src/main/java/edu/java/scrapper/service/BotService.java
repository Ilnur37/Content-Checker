package edu.java.scrapper.service;

import edu.java.scrapper.client.BotClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {
    private final BotClient botClient;

    public ResponseEntity<Void> sendUpdate(int id, String url, String description, List<Integer> tgChatIds) {
        return botClient.sendUpdate(id, url, description, tgChatIds);
    }
}
