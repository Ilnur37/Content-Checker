package edu.java.bot.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class LinkUpdateRequest {
    @Min(1)
    private long id;
    @NotBlank(message = "This field can not be null")
    private String url;

    @NotNull(message = "This field can not be null")
    private String description;

    @NotEmpty(message = "Array is empty")
    private List<Long> tgChatIds;
}