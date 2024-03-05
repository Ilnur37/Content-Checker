package edu.java.scrapper.service.jdbc;

import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.scrapper.dao.ChatDao;
import edu.java.scrapper.exception.custom.ReRegistrationException;
import edu.java.scrapper.model.chat.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static java.time.OffsetDateTime.now;

@Service
@RequiredArgsConstructor
@Transactional
public class JdbcChatService {
    private static final String EXCEPTION = "tg_chat_id=%d";
    private final ChatDao chatDao;

    public void register(long tgChatId) {
        if (chatDao.getByTgChatId(tgChatId).isPresent()) {
            throw new ReRegistrationException(getExMsg(tgChatId));
        }
        Chat chat = new Chat();
        chat.setTgChatId(tgChatId);
        chat.setCreatedAt(now());
        chatDao.save(chat);
    }

    public void unregister(long tgChatId) {
        chatDao.getByTgChatId(tgChatId)
            .orElseThrow(
                () -> new ChatIdNotFoundException(getExMsg(tgChatId))
            );
        Chat chat = new Chat();
        chat.setTgChatId(tgChatId);
        chatDao.delete(chat);
    }

    private String getExMsg(long id) {
        return String.format(EXCEPTION, id);
    }
}
