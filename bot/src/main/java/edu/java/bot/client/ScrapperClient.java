package edu.java.bot.client;

import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.models.dto.response.ApiErrorResponse;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
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
            .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(error -> {
                    log.error(error.getDescription() + SPASE + error.getExceptionMessage());
                    return Mono.error(new RuntimeException(error.getCode()));
                }))
            .bodyToMono(Void.class)
            .block();
    }

    public void deleteChat(long id) {
        webClient.delete()
            .uri(URL_TG_CHAT_ID, id)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(error -> {
                    log.error(error.getDescription() + SPASE + error.getExceptionMessage());
                    return Mono.error(new RuntimeException(error.getCode()));
                }))
            .bodyToMono(Void.class)
            .block();
    }

    public ListLinksResponse getAllLinks(long id) {
        return webClient.get()
            .uri(URL_LINKS)
            .header(HEADER_TG_CHAT_ID, String.valueOf(id))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(error -> {
                    log.error(error.getDescription() + SPASE + error.getExceptionMessage());
                    return Mono.error(new RuntimeException(error.getCode()));
                }))
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(long id, String link) {
        return webClient.post()
            .uri(URL_LINKS)
            .header(HEADER_TG_CHAT_ID, String.valueOf(id))
            .body(BodyInserters.fromValue(new AddLinkRequest(link)))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(error -> {
                    log.error(error.getDescription() + SPASE + error.getExceptionMessage());
                    return Mono.error(new RuntimeException(error.getCode()));
                }))
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse removeLink(long id, String link) {
        return webClient.method(HttpMethod.DELETE)
            .uri(URL_LINKS)
            .header(HEADER_TG_CHAT_ID, String.valueOf(id))
            .body(BodyInserters.fromValue(new RemoveLinkRequest(link)))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(error -> {
                    log.error(error.getDescription() + SPASE + error.getExceptionMessage());
                    return Mono.error(new RuntimeException(error.getCode()));
                }))
            .bodyToMono(LinkResponse.class)
            .block();
    }
}
