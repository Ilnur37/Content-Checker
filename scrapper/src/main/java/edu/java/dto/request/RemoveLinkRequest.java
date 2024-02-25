package edu.java.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveLinkRequest {
    @NotBlank(message = "This field can not be null")
    private String link;
}
