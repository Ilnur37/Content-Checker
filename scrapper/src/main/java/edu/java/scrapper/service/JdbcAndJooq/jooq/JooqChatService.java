package edu.java.scrapper.service.JdbcAndJooq.jooq;

import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.ReRegistrationException;
import edu.java.scrapper.domain.jooq.dao.JooqChatDao;
import edu.java.scrapper.domain.jooq.dao.JooqChatLinkDao;
import edu.java.scrapper.domain.jooq.dao.JooqLinkDao;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.Chat;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.ChatLink;
import edu.java.scrapper.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static java.time.OffsetDateTime.now;

@Service
@RequiredArgsConstructor
@Transactional
public class JooqChatService implements ChatService {
    private final JooqChatDao chatDao;
    private final JooqLinkDao linkDao;
    private final JooqChatLinkDao chatLinkDao;

    @Override
    public void register(long tgChatId) {
        if (chatDao.getByTgChatId(tgChatId).isPresent()) {
            throw new ReRegistrationException(toExMsg(tgChatId));
        }
        Chat chat = new Chat();
        chat.setTgChatId(tgChatId);
        chat.setCreatedAt(now());
        chatDao.save(chat);
    }

    @Override
    public void unregister(long tgChatId) {
        long id = chatDao.getByTgChatId(tgChatId)
            .orElseThrow(
                () -> new ChatIdNotFoundException(toExMsg(tgChatId))
            ).getId();
        //Удаление связанных ссылок
        List<ChatLink> links = chatLinkDao.getByChatId(id);
        for (ChatLink chatLink : links) {
            long linkId = chatLink.getLinkId();
            int countChatTrackLink = chatLinkDao.getByLinkId(linkId).size();
            chatLinkDao.delete(id, linkId);
            //Если ссылку отслеживает 1 чат, удалить из таблицы ссылок
            if (countChatTrackLink == 1) {
                linkDao.deleteById(linkId);
            }
        }
        chatDao.delete(tgChatId);
    }
}
