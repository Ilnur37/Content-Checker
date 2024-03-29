package edu.java.scrapper.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatLinkWithUrl {
    private long chatId;
    private long linkId;
    private String url;
}
