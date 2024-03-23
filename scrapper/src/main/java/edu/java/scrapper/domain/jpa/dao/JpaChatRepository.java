package edu.java.scrapper.domain.jpa.dao;

import edu.java.scrapper.domain.jpa.model.Chat;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findChatByTgChatId(long tgChatId);

    @EntityGraph(attributePaths = "links")
    Optional<Chat> findChatWithLinkByTgChatId(long tgChatId);

    @EntityGraph(attributePaths = "links")
    Optional<Chat> findChatWithLinkById(long id);

    void deleteByTgChatId(long tgChatId);
}
