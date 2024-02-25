package edu.java.bot.service;

import edu.java.bot.dto.request.scrapper.AddLinkRequest;
import edu.java.bot.dto.response.scrapper.LinkResponse;
import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ScrapperService {
    private final WebClient scrapperClient;
    private final String urlTgChatId = "/scrapper-api/tg-chat/{id}";
    private final String urlLinks = "/scrapper-api/links";
    private final String headerTgChatId = "Tg-Chat-Id";

    public ScrapperService(WebClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    public ResponseEntity<Void> registerChat(int id) {
        return scrapperClient.post()
            .uri(urlTgChatId, id)
            .retrieve()
            .toEntity(Void.class)
            .block();
    }

    public ResponseEntity<Void> deleteChat(int id) {
        return scrapperClient.delete()
            .uri(urlTgChatId, id)
            .retrieve()
            .toEntity(Void.class)
            .block();
    }

    public ResponseEntity<List<LinkResponse>> getAllLinks(int id) {
        return scrapperClient.get()
            .uri(urlLinks)
            .header(headerTgChatId, String.valueOf(id))
            .retrieve()
            .toEntityList(LinkResponse.class)
            .block();
    }

    public ResponseEntity<LinkResponse> addLink(int id, String link) {
        return scrapperClient.post()
            .uri(urlLinks)
            .header(headerTgChatId, String.valueOf(id))
            .body(BodyInserters.fromValue(new AddLinkRequest(link)))
            .retrieve()
            .toEntity(LinkResponse.class)
            .block();
    }

    public ResponseEntity<LinkResponse> removeLink(int id, String link) {
        return scrapperClient.method(HttpMethod.DELETE)
            .uri(urlLinks)
            .header(headerTgChatId, String.valueOf(id))
            .body(BodyInserters.fromValue(new AddLinkRequest(link)))
            .retrieve()
            .toEntity(LinkResponse.class)
            .block();
    }
}
