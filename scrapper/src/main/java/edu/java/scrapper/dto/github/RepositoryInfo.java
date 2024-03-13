package edu.java.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepositoryInfo {
    private Long id;
    private String name;
    @JsonProperty("owner")
    private Actor actor;
}
