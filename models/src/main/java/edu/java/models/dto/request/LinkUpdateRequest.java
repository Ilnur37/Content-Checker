package edu.java.models.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record LinkUpdateRequest(
    @Min(1)
    long id,
    @NotBlank(message = "This field can not be null")
    String url,
    @NotNull(message = "This field can not be null")
    String description,
    @NotEmpty(message = "Array is empty")
    List<Long> tgChatIds) {
}
