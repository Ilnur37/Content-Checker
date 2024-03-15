package edu.java.scrapper.service.JdbcAndJooq.jdbc;

import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.ReRegistrationException;
import edu.java.scrapper.domain.jdbc.dao.JdbcChatDao;
import edu.java.scrapper.domain.jdbc.dao.JdbcChatLinkDao;
import edu.java.scrapper.domain.jdbc.dao.JdbcLinkDao;
import edu.java.scrapper.domain.jdbc.model.chat.Chat;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLink;
import edu.java.scrapper.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static java.time.OffsetDateTime.now;

@Service
@RequiredArgsConstructor
@Transactional
public class JdbcChatService implements ChatService {
    private final JdbcChatDao chatDao;
    private final JdbcLinkDao linkDao;
    private final JdbcChatLinkDao chatLinkDao;

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
        long id = chatDao.findByTgChatId(tgChatId)
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
