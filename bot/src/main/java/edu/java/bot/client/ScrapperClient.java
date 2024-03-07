package edu.java.bot.client;

import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;
import org.springframework.http.HttpMethod;
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
        WebClient webClient = WebClient.create(baseUrl);
        return new ScrapperClient(webClient);
    }

    public void registerChat(long id) {
        webClient.post()
            .uri(urlTgChatId, id)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public void deleteChat(long id) {
        webClient.delete()
            .uri(urlTgChatId, id)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public ListLinksResponse getAllLinks(long id) {
        return webClient.get()
            .uri(urlLinks)
            .header(headerTgChatId, String.valueOf(id))
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(long id, String link) {
        return webClient.post()
            .uri(urlLinks)
            .header(headerTgChatId, String.valueOf(id))
            .body(BodyInserters.fromValue(new AddLinkRequest(link)))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse removeLink(long id, String link) {
        return webClient.method(HttpMethod.DELETE)
            .uri(urlLinks)
            .header(headerTgChatId, String.valueOf(id))
            .body(BodyInserters.fromValue(new RemoveLinkRequest(link)))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }
}
