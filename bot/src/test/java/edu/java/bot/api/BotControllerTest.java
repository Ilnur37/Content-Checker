package edu.java.bot.api;

import edu.java.bot.controller.BotController;
import edu.java.models.dto.request.LinkUpdateRequest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

public class BotControllerTest {

    private final WebTestClient webTestClient = WebTestClient.bindToController(new BotController()).build();
    private final long defaultId = 1;
    private final String defaultUrl = "aa";
    private final String defaultDescription = "aa";
    private final List<Long> defaultList = List.of(1L, 2L);

    private LinkUpdateRequest getLinkUpdateRequest(long id, String url, String description, List<Long> tgChatIds) {
        return new LinkUpdateRequest(id, url, description, tgChatIds);
    }

    private WebTestClient.RequestHeadersSpec<?> createPostResponse(LinkUpdateRequest linkUpdateRequest) {
        return webTestClient.post()
            .uri("/bot-api/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(linkUpdateRequest));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 10L, 1000L, 1000000L})
    @DisplayName("Корректные данные ID")
    public void sendUpdateValidId(long id) {
        LinkUpdateRequest linkUpdateRequest = getLinkUpdateRequest(id, defaultUrl, defaultDescription, defaultList);
        createPostResponse(linkUpdateRequest)
            .exchange().expectStatus().isOk();
    }

    @ParameterizedTest
    @ValueSource(strings = {"aa", " aa", "aa ", "aa aa"})
    @DisplayName("Корректные данные url")
    public void sendUpdateValidUrl(String url) {
        LinkUpdateRequest linkUpdateRequest = getLinkUpdateRequest(defaultId, url, defaultDescription, defaultList);
        createPostResponse(linkUpdateRequest)
            .exchange().expectStatus().isOk();
    }

    @ParameterizedTest
    @ValueSource(strings = {"aa", " aa", "aa ", "aa aa"})
    @DisplayName("Корректные данные description")
    public void sendUpdateValidDescription(String description) {
        LinkUpdateRequest linkUpdateRequest = getLinkUpdateRequest(defaultId, defaultUrl, description, defaultList);
        createPostResponse(linkUpdateRequest)
            .exchange().expectStatus().isOk();
    }

    @Test
    @DisplayName("Список tgChatId пуст")
    public void sendUpdateTgChatIdsIsEmpty() {
        LinkUpdateRequest linkUpdateRequest =
            getLinkUpdateRequest(defaultId, defaultUrl, defaultUrl, new ArrayList<>());
        createPostResponse(linkUpdateRequest)
            .exchange().expectStatus().isBadRequest();
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -10L, -1000L, -1000000L})
    @DisplayName("Id меньше 1")
    public void sendUpdateWhenIdLessThenOne(long id) {
        LinkUpdateRequest linkUpdateRequest = getLinkUpdateRequest(id, defaultUrl, defaultDescription, defaultList);
        createPostResponse(linkUpdateRequest)
            .exchange().expectStatus().isBadRequest();
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("url - null")
    public void sendUpdateWhenUrlIsNull(String url) {
        LinkUpdateRequest linkUpdateRequest = getLinkUpdateRequest(defaultId, url, defaultDescription, defaultList);
        createPostResponse(linkUpdateRequest)
            .exchange().expectStatus().isBadRequest();
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("description - null")
    public void sendUpdateWhenDescriptionIsNull(String description) {
        LinkUpdateRequest linkUpdateRequest = getLinkUpdateRequest(defaultId, defaultUrl, description, defaultList);
        createPostResponse(linkUpdateRequest)
            .exchange().expectStatus().isBadRequest();
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("tgChatIds - null")
    public void sendUpdateWhenTgChatIdsIsNull(List<Long> tgChatIds) {
        LinkUpdateRequest linkUpdateRequest =
            getLinkUpdateRequest(defaultId, defaultUrl, defaultDescription, tgChatIds);
        createPostResponse(linkUpdateRequest)
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Url имеет пустое значение")
    public void sendUpdateWhenUrlIsEmpty() {
        LinkUpdateRequest linkUpdateRequest = getLinkUpdateRequest(defaultId, "", defaultDescription, defaultList);
        createPostResponse(linkUpdateRequest)
            .exchange().expectStatus().isBadRequest();
    }
}
