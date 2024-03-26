package edu.java.scrapper.service;

import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;

public interface LinkService {
    String EX_CHAT = "tg_chat_id = ";
    String EX_LINK = "url = ";
    String EMPTY_STRING = "";

    ListLinksResponse getAll(long tgChatId);

    LinkResponse add(long tgChatId, AddLinkRequest linkRequest);

    LinkResponse remove(long tgChatId, RemoveLinkRequest linkRequest);

    default String toExMsg(String ex, String value) {
        return ex + value;
    }
}
