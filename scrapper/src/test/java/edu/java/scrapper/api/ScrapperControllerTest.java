package edu.java.scrapper.api;

import edu.java.scrapper.controller.ScrapperController;
import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

public class ScrapperControllerTest {
    private final WebTestClient webTestClient = WebTestClient.bindToController(new ScrapperController()).build();
    private final String defaultLink = "aa";
    private final int defaultId = 1;

    private AddLinkRequest getAddLinkRequest(String link) {
        AddLinkRequest addLinkRequest = new AddLinkRequest();
        addLinkRequest.setLink(link);
        return addLinkRequest;
    }

    private RemoveLinkRequest getRemoveLinkRequest(String link) {
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest();
        removeLinkRequest.setLink(link);
        return removeLinkRequest;
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 1000000})
    @DisplayName("Регистрация чата, корректные данные")
    void registerChat(int id) {
        webTestClient.post()
            .uri("/scrapper-api/tg-chat/{id}", id)
            .exchange().expectStatus().isOk();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 1000000})
    @DisplayName("Удаление чата, корректные данные")
    void deleteChat(int id) {
        webTestClient.delete()
            .uri("/scrapper-api/tg-chat/{id}", id)
            .exchange().expectStatus().isOk();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 1000000})
    @DisplayName("Получить все отслеживаемые ссылки, корректные данные")
    void getAllLinks(int id) {
        webTestClient.get()
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(id))
            .exchange().expectStatus().isOk();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 1000000})
    @DisplayName("Добавить ссылку, корректные данные")
    void addLink(int id) {
        webTestClient.post()
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(id))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(getAddLinkRequest(defaultLink)))
            .exchange().expectStatus().isOk();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 1000000})
    @DisplayName("Удалить ссылку, корректные данные")
    void removeLink(int id) {
        webTestClient.method(HttpMethod.DELETE)
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(id))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(getRemoveLinkRequest(defaultLink)))
            .exchange().expectStatus().isOk();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10, -1000, -1000000})
    @DisplayName("Регистрация чата, не корректные данные ID")
    void registerChatWhenInvalidId(int id) {
        webTestClient.post()
            .uri("/scrapper-api/tg-chat/{id}", id)
            .exchange().expectStatus().isBadRequest();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10, -1000, -1000000})
    @DisplayName("Удаление чата, не корректные данные ID")
    void deleteChatWhenInvalidId(int id) {
        webTestClient.delete()
            .uri("/scrapper-api/tg-chat/{id}", id)
            .exchange().expectStatus().isBadRequest();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10, -1000, -1000000})
    @DisplayName("Получить все отслеживаемые ссылки, не корректные данные ID")
    void getAllLinksWhenInvalidId(int id) {
        webTestClient.get()
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(id))
            .exchange().expectStatus().isBadRequest();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10, -1000, -1000000})
    @DisplayName("Добавить ссылку, не корректные данные")
    void addLinkWhenInvalidId(int id) {
        webTestClient.post()
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(id))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(getAddLinkRequest(defaultLink)))
            .exchange().expectStatus().isBadRequest();
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Добавить ссылку, ссылка null")
    void addLinkWhenLinkIsNull(String link) {
        webTestClient.post()
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(defaultId))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(getAddLinkRequest(link)))
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Добавить ссылку, ссылка пустая")
    void addLinkWhenLinkIsEmpty() {
        webTestClient.post()
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(defaultId))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(getAddLinkRequest("")))
            .exchange().expectStatus().isBadRequest();
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Удалаить ссылку, ссылка null")
    void removeLinkWhenLinkIsNull(String link) {
        webTestClient.method(HttpMethod.DELETE)
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(defaultId))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(getRemoveLinkRequest(link)))
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Удалаить ссылку, ссылка пустая")
    void removeLinkWhenLinkIsEmpty() {
        webTestClient.method(HttpMethod.DELETE)
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(defaultId))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(getRemoveLinkRequest("")))
            .exchange().expectStatus().isBadRequest();
    }
}
