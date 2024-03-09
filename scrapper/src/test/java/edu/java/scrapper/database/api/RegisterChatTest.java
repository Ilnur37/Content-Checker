package edu.java.scrapper.database.api;

import edu.java.models.dto.response.ApiErrorResponse;
import edu.java.scrapper.controller.ScrapperController;
import edu.java.scrapper.database.IntegrationTest;
import edu.java.scrapper.service.jdbc.JdbcChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@SpringBootTest
@Rollback
@Transactional
public class RegisterChatTest extends IntegrationTest {
    @Autowired
    private JdbcChatService chatService;
    private final WebTestClient webTestClient;
    private final String defaultLink = "aa";
    private final int defaultId = 1;

    @Autowired
    public RegisterChatTest(ScrapperController scrapperController) {
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
    @ValueSource(longs = {0L, -1L, -10L, -1000L, -1000000L})
    @DisplayName("Регистрация чата, не корректные данные ID")
    void registerChatWhenInvalidId(long id) {
        Flux<ApiErrorResponse> response = webTestClient.post()
            .uri("/scrapper-api/tg-chat/{id}", id)
            .exchange()
            .expectStatus().isBadRequest()
            .returnResult(ApiErrorResponse.class).getResponseBody();
        System.out.println(1);
    }

    /*@ParameterizedTest
    @ValueSource(longs = {1L, 10L, 1000L, 1000000L})
    @DisplayName("Регистрация чата, чат с заданным id уже существует")
    void registerChatWhenReRegistration(long id) {
        webTestClient.post()
            .uri("/scrapper-api/tg-chat/{id}", id)
            .exchange().expectStatus().isCreated();
        webTestClient.post()
            .uri("/scrapper-api/tg-chat/{id}", id)
            .exchange().expectStatus().is4xxClientError();
    }*/
}
