package edu.java.service;

import edu.java.dto.request.bot.LinkUpdateRequest;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BotService {
    private final WebClient botClient;

    public BotService(WebClient botClient) {
        this.botClient = botClient;
    }

    public ResponseEntity<Void> sendUpdate(int id, String url, String description, List<Integer> tgChatIds) {
        return botClient.post()
            .uri("/bot-api/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new LinkUpdateRequest(id, url, description, tgChatIds)))
            .retrieve()
            .toEntity(Void.class)
            .block();
    }
}
