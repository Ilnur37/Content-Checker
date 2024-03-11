package edu.java.scrapper.service;

public interface ChatService {
    String EXCEPTION = "tg_chat_id = %d";

    void register(long tgChatId);

    void unregister(long tgChatId);

    default String toExMsg(long id) {
        return String.format(EXCEPTION, id);
    }
}
