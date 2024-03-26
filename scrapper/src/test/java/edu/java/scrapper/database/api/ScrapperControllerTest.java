package edu.java.scrapper.database.api;

import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.scrapper.controller.ScrapperController;
import edu.java.scrapper.database.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

public class ScrapperControllerTest extends IntegrationTest {
    private final WebTestClient webTestClient;

    @Autowired
    public ScrapperControllerTest(ScrapperController scrapperController) {
        webTestClient = WebTestClient.bindToController(scrapperController).build();
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 10L, 1000L, 1000000L})
    @DisplayName("Регистрация чата, корректные данные")
    void registerChat(long id) {
        webTestClient.post()
            .uri("/scrapper-api/tg-chat/{id}", id)
            .exchange().expectStatus().isCreated();
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 10L, 1000L, 1000000L})
    @DisplayName("Удаление чата, корректные данные")
    void deleteChat(long id) {
        webTestClient.delete()
            .uri("/scrapper-api/tg-chat/{id}", id)
            .exchange().expectStatus().isOk();
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 10L, 1000L, 1000000L})
    @DisplayName("Получить все отслеживаемые ссылки, корректные данные")
    void getAllLinks(long id) {
        webTestClient.get()
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(id))
            .exchange().expectStatus().isOk();
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 10L, 1000L, 1000000L})
    @DisplayName("Добавить ссылку, корректные данные")
    void addLink(long id) {
        webTestClient.post()
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(id))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new AddLinkRequest(defaultUrl)))
            .exchange().expectStatus().isOk();
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 10L, 1000L, 1000000L})
    @DisplayName("Удалить ссылку, корректные данные")
    void removeLink(long id) {
        webTestClient.method(HttpMethod.DELETE)
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(id))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new RemoveLinkRequest(defaultUrl)))
            .exchange().expectStatus().isOk();
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -10L, -1000L, -1000000L})
    @DisplayName("Регистрация чата, не корректные данные ID")
    void registerChatWhenInvalidId(long id) {
        webTestClient.post()
            .uri("/scrapper-api/tg-chat/{id}", id)
            .exchange().expectStatus().isBadRequest();
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -10L, -1000L, -1000000L})
    @DisplayName("Удаление чата, не корректные данные ID")
    void deleteChatWhenInvalidId(long id) {
        webTestClient.delete()
            .uri("/scrapper-api/tg-chat/{id}", id)
            .exchange().expectStatus().isBadRequest();
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -10L, -1000L, -1000000L})
    @DisplayName("Получить все отслеживаемые ссылки, не корректные данные ID")
    void getAllLinksWhenInvalidId(long id) {
        webTestClient.get()
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(id))
            .exchange().expectStatus().isBadRequest();
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -10L, -1000L, -1000000L})
    @DisplayName("Добавить ссылку, не корректные данные")
    void addLinkWhenInvalidId(long id) {
        webTestClient.post()
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(id))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new AddLinkRequest(defaultUrl)))
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
            .body(BodyInserters.fromValue(new AddLinkRequest(link)))
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Добавить ссылку, ссылка пустая")
    void addLinkWhenLinkIsEmpty() {
        webTestClient.post()
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(defaultId))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new AddLinkRequest("")))
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
            .body(BodyInserters.fromValue(new RemoveLinkRequest(link)))
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Удалаить ссылку, ссылка пустая")
    void removeLinkWhenLinkIsEmpty() {
        webTestClient.method(HttpMethod.DELETE)
            .uri("/scrapper-api/links")
            .header("Tg-Chat-Id", String.valueOf(defaultId))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new RemoveLinkRequest("")))
            .exchange().expectStatus().isBadRequest();
    }
}
