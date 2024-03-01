package edu.java.bot.controller;

import edu.java.models.dto.request.LinkUpdateRequest;
import edu.java.models.dto.response.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface BotApi {
    @Operation(summary = "Отправить обновление", description = "", tags = {})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Обновление обработано"),

        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class))),

        @ApiResponse(responseCode = "404",
                     description = "Чат с заданным id не найден",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiErrorResponse.class)))})
    @PostMapping("/updates")
    ResponseEntity<Void> sendUpdate(@Valid @RequestBody LinkUpdateRequest linkUpdateRequest);
}
