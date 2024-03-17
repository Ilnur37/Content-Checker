package edu.java.scrapper.domain.jdbc.model.chat;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Chat {
    private long id;
    private long tgChatId;
    private OffsetDateTime createdAt;

    public static Chat createChat(long tgChatId, OffsetDateTime dateTime) {
        Chat chat = new Chat();
        chat.setTgChatId(tgChatId);
        chat.setCreatedAt(dateTime);
        return chat;
    }
}
