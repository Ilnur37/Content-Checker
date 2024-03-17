package edu.java.scrapper.controller;

import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.models.dto.response.ApiErrorResponse;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import static edu.java.scrapper.controller.ScrapperController.SCRAPPER_MAPPING;

@RestController
@RequiredArgsConstructor
@RequestMapping(SCRAPPER_MAPPING)
public class ScrapperController {
    public static final String TG_CHAT_ID_MAPPING = "/tg-chat/{id}";
    public static final String LINK_MAPPING = "/links";
    public static final String SCRAPPER_MAPPING = "scrapper-api";

    private final ChatService chatService;
    private final LinkService linkService;

    @Operation(summary = "Зарегистрировать чат", description = "Created")
    @ApiResponse(responseCode = "201", description = "Чат зарегистрирован")
    @ApiResponse(
        responseCode = "400", description = "Некорректные параметры запроса",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(
        responseCode = "409", description = "Чат с заданным id уже существует",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping(TG_CHAT_ID_MAPPING)
    @ResponseStatus(HttpStatus.CREATED)
    public void registerChat(@Min(1) @PathVariable long id) {
        chatService.register(id);
    }

    @Operation(summary = "Удалить чат", description = "Ok")
    @ApiResponse(responseCode = "200", description = "Чат успешно удалён")
    @ApiResponse(
        responseCode = "400", description = "Некорректные параметры запроса",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(
        responseCode = "404", description = "Чат не существует",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @DeleteMapping(TG_CHAT_ID_MAPPING)
    @ResponseStatus(HttpStatus.OK)
    public void deleteChat(@Min(1) @PathVariable long id) {
        chatService.unregister(id);
    }

    @Operation(summary = "Получить все отслеживаемые ссылки", description = "Ok")
    @ApiResponse(
        responseCode = "200", description = "Ссылки успешно получены",
        content = @Content(schema = @Schema(implementation = ListLinksResponse.class)))
    @ApiResponse(
        responseCode = "400", description = "Некорректные параметры запроса",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(
        responseCode = "404", description = "Чат не существует",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping(LINK_MAPPING)
    @ResponseStatus(HttpStatus.OK)
    public ListLinksResponse getAllLinks(@Min(1) @RequestHeader("Tg-Chat-Id") long tgChatId) {
        return linkService.getAll(tgChatId);
    }

    @Operation(summary = "Добавить отслеживание ссылки", description = "Ok")
    @ApiResponse(
        responseCode = "200", description = "Ссылка успешно добавлена",
        content = @Content(schema = @Schema(implementation = LinkResponse.class)))
    @ApiResponse(
        responseCode = "400", description = "Некорректные параметры запроса",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(
        responseCode = "404", description = "Чат не существует",
        content = @Content(mediaType = "application/json",
                           schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(
        responseCode = "409", description = "Данная ссылка уже существует",
        content = @Content(mediaType = "application/json",
                           schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping(LINK_MAPPING)
    @ResponseStatus(HttpStatus.OK)
    public LinkResponse addLink(
        @Min(1) @RequestHeader("Tg-Chat-Id") long tgChatId,
        @Valid @RequestBody AddLinkRequest addLinkRequest
    ) {
        return linkService.add(tgChatId, addLinkRequest);
    }

    @Operation(summary = "Убрать отслеживание ссылки", description = "Ok")
    @ApiResponse(
        responseCode = "200", description = "Ссылка успешно убрана",
        content = @Content(schema = @Schema(implementation = LinkResponse.class)))
    @ApiResponse(
        responseCode = "400", description = "Некорректные параметры запроса",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(
        responseCode = "404", description = "Чат не существует",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(
        responseCode = "404", description = "Ссылка не найдена",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(LINK_MAPPING)
    public LinkResponse removeLink(
        @Min(1) @RequestHeader("Tg-Chat-Id") long tgChatId,
        @Valid @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        return linkService.remove(tgChatId, removeLinkRequest);
    }
}
