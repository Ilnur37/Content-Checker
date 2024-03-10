package edu.java.models.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RemoveLinkRequest(@NotBlank(message = "This field can not be null") String link) {
}
