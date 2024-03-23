package edu.java.scrapper.service.jdbc;

import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.ReRegistrationException;
import edu.java.scrapper.domain.jdbc.dao.JdbcChatDao;
import edu.java.scrapper.domain.jdbc.model.chat.Chat;
import edu.java.scrapper.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import static java.time.OffsetDateTime.now;

@RequiredArgsConstructor
@Transactional
public class JdbcChatService implements ChatService {
    private final JdbcChatDao chatDao;

    @Override
    public void register(long tgChatId) {
        if (chatDao.findByTgChatId(tgChatId).isPresent()) {
            throw new ReRegistrationException(toExMsg(tgChatId));
        }
        Chat chat = Chat.createChat(tgChatId, now());
        chatDao.save(chat);
    }

    @Override
    public void unregister(long tgChatId) {
        chatDao.findByTgChatId(tgChatId)
            .orElseThrow(
                () -> new ChatIdNotFoundException(toExMsg(tgChatId))
            );
        chatDao.delete(tgChatId);
    }
}
