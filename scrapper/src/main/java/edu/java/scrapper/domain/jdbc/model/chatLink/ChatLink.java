package edu.java.scrapper.domain.jdbc.model.chatLink;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatLink {
    private long id;
    private long chatId;
    private long linkId;

    public static ChatLink createChatLink(long chatId, long linkId) {
        ChatLink chatLink = new ChatLink();
        chatLink.setChatId(chatId);
        chatLink.setLinkId(linkId);
        return chatLink;
    }
}
