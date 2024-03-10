package edu.java.models.dto.response;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ListLinksResponse(
    @NotBlank(message = "This field can not be null")
    List<LinkResponse> links,
    int size) {
}
