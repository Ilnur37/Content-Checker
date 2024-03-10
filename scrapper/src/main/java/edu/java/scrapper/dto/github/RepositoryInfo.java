package edu.java.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepositoryInfo {
    private Long id;
    @JsonProperty("full_name")
    private String fullName;
    private String url;
    @JsonProperty("pushed_at")
    private OffsetDateTime pushedAt;
}
