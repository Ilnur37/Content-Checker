package edu.java.scrapper.domain;

import java.util.List;
import java.util.Optional;

public interface ChatDao<T> {

    List<T> getAll();

    Optional<T> findByTgChatId(long tgChatId);

    Optional<T> findById(long id);

    int save(T chat);

    int delete(long tgChatId);
}
