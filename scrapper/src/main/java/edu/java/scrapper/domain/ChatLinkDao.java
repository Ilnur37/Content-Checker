package edu.java.scrapper.domain;

import edu.java.scrapper.domain.model.ChatLinkWithTgChat;
import edu.java.scrapper.domain.model.ChatLinkWithUrl;
import java.util.List;

public interface ChatLinkDao<T> {
    List<T> getAll();

    List<T> getByChatId(long id);

    List<ChatLinkWithUrl> getByChatIdJoinLink(long id);

    List<T> getByLinkId(long id);

    List<ChatLinkWithTgChat> getByLinkIdJoinChat(long id);

    int save(T chatLink);

    int delete(long chatId, long linkId);
}
