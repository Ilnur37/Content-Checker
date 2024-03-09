package edu.java.scrapper.service.jdbc;

import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.scrapper.dao.ChatDao;
import edu.java.scrapper.dao.ChatLinkDao;
import edu.java.scrapper.dao.LinkDao;
import edu.java.scrapper.exception.custom.ReRegistrationException;
import edu.java.scrapper.model.chat.Chat;
import edu.java.scrapper.model.chatLink.ChatLink;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static java.time.OffsetDateTime.now;

@Service
@RequiredArgsConstructor
@Transactional
public class JdbcChatService {
    private static final String EXCEPTION = "tg_chat_id = %d";
    private final ChatDao chatDao;
    private final LinkDao linkDao;
    private final ChatLinkDao chatLinkDao;

    public void register(long tgChatId) {
        if (chatDao.getByTgChatId(tgChatId).isPresent()) {
            throw new ReRegistrationException(toExMsg(tgChatId));
        }
        Chat chat = new Chat();
        chat.setTgChatId(tgChatId);
        chat.setCreatedAt(now());
        chatDao.save(chat);
    }

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

    private String toExMsg(long id) {
        return String.format(EXCEPTION, id);
    }
}
