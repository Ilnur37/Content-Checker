package edu.java.scrapper.domain.jdbc.model.link;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Link {
    private long id;
    private String url;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastUpdateAt;
}
