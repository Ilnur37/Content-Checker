package edu.java.scrapper.client;

import edu.java.models.dto.request.LinkUpdateRequest;
import edu.java.models.dto.response.ApiErrorResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class BotClient extends Client {
    private static final String SPASE = " ";

    public BotClient(WebClient webClient) {
        super(webClient);
    }

    public static BotClient create(String baseUrl) {
        WebClient webClient = WebClient.create(baseUrl);
        return new BotClient(webClient);
    }

    public void sendUpdate(long id, String url, String description, List<Long> tgChatIds) {
        webClient.post()
            .uri("/bot-api/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new LinkUpdateRequest(id, url, description, tgChatIds))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(error -> {
                    log.error(error.getDescription() + SPASE + error.getExceptionMessage());
                    return Mono.error(new RuntimeException(error.getCode() + SPASE + error.getExceptionMessage()));
                }))
            .bodyToMono(Void.class)
            .block();
    }
}
