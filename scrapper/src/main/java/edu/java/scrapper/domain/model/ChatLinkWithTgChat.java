package edu.java.scrapper.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatLinkWithTgChat {
    private long chatId;
    private long linkId;
    private long tgChatId;
}
