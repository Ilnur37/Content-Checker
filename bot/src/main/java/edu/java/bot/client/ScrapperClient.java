package edu.java.bot.client;

import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.models.dto.response.ApiErrorResponse;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.LinkNotFoundException;
import edu.java.models.exception.ReAddLinkException;
import edu.java.models.exception.ReRegistrationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class ScrapperClient extends Client {
    private static final String URL_TG_CHAT_ID = "/scrapper-api/tg-chat/{id}";
    private static final String URL_LINKS = "/scrapper-api/links";
    private static final String HEADER_TG_CHAT_ID = "Tg-Chat-Id";
    private static final String SPASE = " ";

    public ScrapperClient(WebClient webClient) {
        super(webClient);
    }

    public static ScrapperClient create(String baseUrl) {
        WebClient webClient = WebClient.create(baseUrl);
        return new ScrapperClient(webClient);
    }

    public void registerChat(long id) {
        webClient.post()
            .uri(URL_TG_CHAT_ID, id)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handleApiError)
            .bodyToMono(Void.class)
            .block();
    }

    public void deleteChat(long id) {
        webClient.delete()
            .uri(URL_TG_CHAT_ID, id)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handleApiError)
            .bodyToMono(Void.class)
            .block();
    }

    public ListLinksResponse getAllLinks(long id) {
        return webClient.get()
            .uri(URL_LINKS)
            .header(HEADER_TG_CHAT_ID, String.valueOf(id))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handleApiError)
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(long id, String link) {
        return webClient.post()
            .uri(URL_LINKS)
            .header(HEADER_TG_CHAT_ID, String.valueOf(id))
            .bodyValue(new AddLinkRequest(link))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handleApiError)
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse removeLink(long id, String link) {
        return webClient.method(HttpMethod.DELETE)
            .uri(URL_LINKS)
            .header(HEADER_TG_CHAT_ID, String.valueOf(id))
            .bodyValue(new RemoveLinkRequest(link))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handleApiError)
            .bodyToMono(LinkResponse.class)
            .block();
    }

    private Mono<Exception> handleApiError(ClientResponse response) {
        return response.bodyToMono(ApiErrorResponse.class)
            .flatMap(error -> {
                log.error(error.getDescription() + SPASE + error.getExceptionMessage());
                return Mono.error(getCustomApiException(error));
            });
    }

    @SuppressWarnings({"ReturnCount", "MissingSwitchDefault"})
    private RuntimeException getCustomApiException(ApiErrorResponse apiErrorResponse) {
        String msg = apiErrorResponse.getExceptionMessage();
        String code = apiErrorResponse.getCode();
        switch (apiErrorResponse.getReasonOfError()) {
            case CHAT -> {
                if (code.equals(HttpStatus.CONFLICT.toString())) {
                    return new ReRegistrationException(msg);
                }
                if (code.equals(HttpStatus.NOT_FOUND.toString())) {
                    return new ChatIdNotFoundException(msg);
                }
            }
            case LINK -> {
                if (code.equals(HttpStatus.CONFLICT.toString())) {
                    return new ReAddLinkException(msg);
                }
                if (code.equals(HttpStatus.NOT_FOUND.toString())) {
                    return new LinkNotFoundException(msg);
                }
            }
            case ELSE -> {
                return new IllegalArgumentException();
            }
        }
        return new RuntimeException();
    }
}
