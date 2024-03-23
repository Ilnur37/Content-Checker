package edu.java.scrapper.service.jpa;

import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.ReRegistrationException;
import edu.java.scrapper.domain.jpa.dao.JpaChatRepository;
import edu.java.scrapper.domain.jpa.model.Chat;
import edu.java.scrapper.service.ChatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import static java.time.OffsetDateTime.now;

@RequiredArgsConstructor
@Transactional
public class JpaChatService implements ChatService {

    private final JpaChatRepository chatRepository;

    @Override
    public void register(long tgChatId) {
        if (chatRepository.findChatByTgChatId(tgChatId).isPresent()) {
            throw new ReRegistrationException(toExMsg(tgChatId));
        }
        Chat chat = new Chat();
        chat.setTgChatId(tgChatId);
        chat.setCreatedAt(now());
        chatRepository.save(chat);
    }

    @Override
    public void unregister(long tgChatId) {
        chatRepository.findChatByTgChatId(tgChatId)
            .orElseThrow(
                () -> new ChatIdNotFoundException(toExMsg(tgChatId))
            );
        chatRepository.deleteByTgChatId(tgChatId);
    }
}
