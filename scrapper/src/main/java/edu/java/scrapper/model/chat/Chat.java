package edu.java.scrapper.model.chat;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Chat {
    private long id;
    private long tgChatId;
    private OffsetDateTime createdAt;
}
