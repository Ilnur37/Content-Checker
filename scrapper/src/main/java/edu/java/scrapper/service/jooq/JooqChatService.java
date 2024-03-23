package edu.java.scrapper.service.jooq;

import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.ReRegistrationException;
import edu.java.scrapper.domain.jooq.dao.JooqChatDao;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.Chat;
import edu.java.scrapper.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import static java.time.OffsetDateTime.now;

@RequiredArgsConstructor
@Transactional
public class JooqChatService implements ChatService {

    private final JooqChatDao chatDao;

    @Override
    public void register(long tgChatId) {
        if (chatDao.findByTgChatId(tgChatId).isPresent()) {
            throw new ReRegistrationException(toExMsg(tgChatId));
        }
        Chat chat = new Chat();
        chat.setTgChatId(tgChatId);
        chat.setCreatedAt(now());
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
