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
    private String name;
    private String author;
    private OffsetDateTime lastCheckAt;

    public static Link createLink(
        String url,
        OffsetDateTime createdAt,
        OffsetDateTime lastUpdateAt,
        String name,
        String author,
        OffsetDateTime lastCheckAt
    ) {
        Link link = new Link();
        link.setUrl(url);
        link.setCreatedAt(createdAt);
        link.setLastUpdateAt(lastUpdateAt);
        link.setName(name);
        link.setAuthor(author);
        link.setLastCheckAt(lastCheckAt);
        return link;
    }
}
