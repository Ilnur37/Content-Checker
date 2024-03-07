package edu.java.bot.controller;

import edu.java.models.dto.request.LinkUpdateRequest;
import edu.java.models.dto.response.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import static edu.java.bot.controller.BotController.BOOT_MAPPING;

@RestController
@RequestMapping(BOOT_MAPPING)
public class BotController {
    public static final String BOOT_MAPPING = "bot-api";
    public static final String UPDATE_MAPPING = "/updates";

    @Operation(summary = "Отправить обновление", description = "Ok")
    @ApiResponse(responseCode = "200", description = "Обновление обработано")
    @ApiResponse(
        responseCode = "400", description = "Некорректные параметры запроса",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @ApiResponse(
        responseCode = "404", description = "Чат с заданным id не найден",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @PostMapping(UPDATE_MAPPING)
    @ResponseStatus(HttpStatus.OK)
    public void sendUpdate(@Valid @RequestBody LinkUpdateRequest linkUpdateRequest) {
        //работа сервиса
    }
}
