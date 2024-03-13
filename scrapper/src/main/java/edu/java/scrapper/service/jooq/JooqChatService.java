package edu.java.scrapper.service.jooq;

import edu.java.scrapper.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JooqChatService implements ChatService {

    @Override
    public void register(long tgChatId) {

    }

    @Override
    public void unregister(long tgChatId) {

    }
}
