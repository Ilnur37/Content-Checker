package edu.java.scrapper.database.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.scrapper.database.IntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class ScrapperControllerTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Rollback
    @Transactional
    @DisplayName("Регистрация чата(201), корректные данные")
    void registerChat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/scrapper-api/tg-chat/{id}", defaultTgChatId))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -10L, -1000L})
    @DisplayName("Регистрация чата(400), не корректные данные ID")
    void registerChatWhenInvalidId(long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/scrapper-api/tg-chat/{id}", id))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Rollback
    @Transactional
    @DisplayName("Регистрация чата(409), повторная регистрация")
    void reRegisterChat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/scrapper-api/tg-chat/{id}", defaultTgChatId))
            .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Rollback
    @Transactional
    @DisplayName("Удаление чата(200), корректные данные")
    void deleteChat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/scrapper-api/tg-chat/{id}", defaultTgChatId))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -10L, -1000L})
    @DisplayName("Удаление чата(400), не корректные данные ID")
    void deleteChatWhenInvalidId(long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/scrapper-api/tg-chat/{id}", id))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Удаление чата(404), чат не существует")
    void deleteChatWhenChatNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/scrapper-api/tg-chat/{id}", defaultTgChatId))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Rollback
    @Transactional
    @DisplayName("Получить все отслеживаемые ссылки(200), корректные данные")
    void getAllLinks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(defaultTgChatId)))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -10L, -1000L, -1000000L})
    @DisplayName("Получить все отслеживаемые ссылки(400), не корректные данные ID")
    void getAllLinksWhenInvalidId(long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(id)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Получить все отслеживаемые ссылки(404), чат не существует")
    void getAllLinksWhenChatNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(defaultTgChatId)))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Rollback
    @Transactional
    @DisplayName("Добавить ссылку(200), корректные данные")
    void addLink() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(defaultTgChatId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddLinkRequest(defaultUrl)))
            )
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -10L, -1000L, -1000000L})
    @DisplayName("Добавить ссылку(400), не корректные данные чата")
    void addLinkWhenInvalidId(long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddLinkRequest(defaultUrl)))
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Добавить ссылку(400), ссылка null")
    void addLinkWhenLinkIsNull(String link) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(defaultTgChatId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddLinkRequest(link)))
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Добавить ссылку(400), ссылка пустая")
    void addLinkWhenLinkIsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(defaultTgChatId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddLinkRequest("")))
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Rollback
    @Transactional
    @DisplayName("Добавить ссылку(404), ссылки не существует(не пингуется)")
    void addLinkWhenLinkIsNotPing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(defaultTgChatId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddLinkRequest(
                    "https://stackoverflow.com/questions/1/how-to-call-restcontrolleradvice-class-from-a-unit-test-in-the-service-layer-of"
                )))
            )
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Rollback
    @Transactional
    @DisplayName("Добавить ссылку(404), чат не существует")
    void addLinkWhenChatNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(defaultTgChatId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddLinkRequest(defaultUrl)))
            )
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    @Rollback
    @Transactional
    @DisplayName("Добавить ссылку(409), повторное добавление")
    void reAddLink() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(defaultTgChatId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddLinkRequest(defaultUrl)))
            )
            .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @Sql(value = "/sql/insertOneRowChat.sql")
    @Sql(value = "/sql/insertOneRowLink.sql")
    @Sql(value = "/sql/insertOneRowChatLink.sql")
    @Rollback
    @Transactional
    @DisplayName("Удалить ссылку(200), корректные данные")
    void removeLink() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(defaultTgChatId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RemoveLinkRequest(defaultUrl)))
            )
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -10L, -1000L, -1000000L})
    @DisplayName("Удалить ссылку(400), не корректные данные чата")
    void removeLinkWhenInvalidId(long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddLinkRequest(defaultUrl)))
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Удалить ссылку(400), ссылка null")
    void removeLinkWhenLinkIsNull(String link) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(defaultTgChatId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RemoveLinkRequest(link)))
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Удалить ссылку(400), ссылка пустая")
    void removeLinkWhenLinkIsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(defaultTgChatId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RemoveLinkRequest("")))
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql(value = "/sql/insertOneRowChat.sql")
    @Rollback
    @Transactional
    @Test
    @DisplayName("Удалить ссылку(404), ссылка не найдена")
    void removeLinkWhenLinkIsNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(defaultTgChatId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RemoveLinkRequest(defaultUrl)))
            )
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Удалить ссылку(404), чат не найден")
    void removeLinkWhenChatIsNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/scrapper-api/links")
                .header("Tg-Chat-Id", String.valueOf(defaultTgChatId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RemoveLinkRequest(defaultUrl)))
            )
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
