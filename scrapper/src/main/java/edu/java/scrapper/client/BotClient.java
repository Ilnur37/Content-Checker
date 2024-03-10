package edu.java.scrapper.client;

import edu.java.models.dto.request.LinkUpdateRequest;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class BotClient extends Client {
    public BotClient(WebClient webClient) {
        super(webClient);
    }

    public static BotClient create(String baseUrl) {
        WebClient webClient = WebClient.create(baseUrl);
        return new BotClient(webClient);
    }

    public void sendUpdate(int id, String url, String description, List<Long> tgChatIds) {
        webClient.post()
            .uri("/bot-api/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new LinkUpdateRequest(id, url, description, tgChatIds))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
