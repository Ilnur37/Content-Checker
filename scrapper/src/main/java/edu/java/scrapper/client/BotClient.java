package edu.java.scrapper.client;

import edu.java.models.dto.request.LinkUpdateRequest;
import edu.java.models.dto.response.ApiErrorResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
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
            .onStatus(HttpStatusCode::is4xxClientError, this::handleApiError)
            .bodyToMono(Void.class)
            .block();
    }

    private Mono<Exception> handleApiError(ClientResponse response) {
        return response.bodyToMono(ApiErrorResponse.class)
            .flatMap(error -> {
                log.error(error.getDescription() + SPASE + error.getExceptionMessage());
                return Mono.error(getCustomApiException(error));
            });
    }

    private RuntimeException getCustomApiException(ApiErrorResponse apiErrorResponse) {
        String msg = apiErrorResponse.getExceptionMessage();
        String code = apiErrorResponse.getCode();
        if (code.equals(HttpStatus.NOT_FOUND.toString())) {
            return new ChatIdNotFoundException(msg);
        }
        if (code.equals(HttpStatus.BAD_REQUEST.toString())) {
            return new IllegalArgumentException();
        }
        return new RuntimeException();
    }
}
