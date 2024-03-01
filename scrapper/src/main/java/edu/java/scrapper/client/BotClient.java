package edu.java.scrapper.client;

import edu.java.models.dto.request.LinkUpdateRequest;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class BotClient extends Client {
    public BotClient(WebClient webClient) {
        super(webClient);
    }

    public static BotClient create(String baseUrl) {
        WebClient webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();

        return new BotClient(webClient);
    }

    public ResponseEntity<Void> sendUpdate(int id, String url, String description, List<Long> tgChatIds) {
        return webClient.post()
            .uri("/bot-api/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new LinkUpdateRequest(id, url, description, tgChatIds)))
            .retrieve()
            .toEntity(Void.class)
            .block();
    }
}
