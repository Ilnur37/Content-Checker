package edu.java.bot.client;

import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class ScrapperClient extends Client {
    private final String urlTgChatId = "/scrapper-api/tg-chat/{id}";
    private final String urlLinks = "/scrapper-api/links";
    private final String headerTgChatId = "Tg-Chat-Id";

    public ScrapperClient(WebClient webClient) {
        super(webClient);
    }

    public static ScrapperClient create(String baseUrl) {
        WebClient webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();

        return new ScrapperClient(webClient);
    }

    public ResponseEntity<Void> registerChat(long id) {
        return webClient.post()
            .uri(urlTgChatId, id)
            .retrieve()
            .toEntity(Void.class)
            .block();
    }

    public ResponseEntity<Void> deleteChat(long id) {
        return webClient.delete()
            .uri(urlTgChatId, id)
            .retrieve()
            .toEntity(Void.class)
            .block();
    }

    public ResponseEntity<ListLinksResponse> getAllLinks(long id) {
        return webClient.get()
            .uri(urlLinks)
            .header(headerTgChatId, String.valueOf(id))
            .retrieve()
            .toEntity(ListLinksResponse.class)
            .block();
    }

    public ResponseEntity<LinkResponse> addLink(long id, String link) {
        return webClient.post()
            .uri(urlLinks)
            .header(headerTgChatId, String.valueOf(id))
            .body(BodyInserters.fromValue(new AddLinkRequest(link)))
            .retrieve()
            .toEntity(LinkResponse.class)
            .block();
    }

    public ResponseEntity<LinkResponse> removeLink(long id, String link) {
        return webClient.method(HttpMethod.DELETE)
            .uri(urlLinks)
            .header(headerTgChatId, String.valueOf(id))
            .body(BodyInserters.fromValue(new RemoveLinkRequest(link)))
            .retrieve()
            .toEntity(LinkResponse.class)
            .block();
    }
}
