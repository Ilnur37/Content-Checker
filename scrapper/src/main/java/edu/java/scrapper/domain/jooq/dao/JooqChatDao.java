package edu.java.scrapper.domain.jooq.dao;

import edu.java.scrapper.domain.ChatDao;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.Chat;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.scrapper.domain.jooq.generate.tables.Chat.CHAT;

@RequiredArgsConstructor
public class JooqChatDao implements ChatDao<Chat> {

    private final DSLContext dsl;

    @Override
    public List<Chat> getAll() {
        return dsl.selectFrom(CHAT)
            .fetchInto(Chat.class);
    }

    @Override
    public Optional<Chat> findByTgChatId(long tgChatId) {
        return dsl.selectFrom(CHAT)
            .where(CHAT.TG_CHAT_ID.eq(tgChatId))
            .fetchOptionalInto(Chat.class);
    }

    @Override
    public Optional<Chat> findById(long id) {
        return dsl.selectFrom(CHAT)
            .where(CHAT.ID.eq(id))
            .fetchOptionalInto(Chat.class);
    }

    @Override
    public int save(Chat newChat) {
        return dsl.insertInto(CHAT)
            .set(dsl.newRecord(CHAT, newChat))
            .execute();
    }

    @Override
    public int delete(long tgChatId) {
        return dsl.deleteFrom(CHAT)
            .where(CHAT.TG_CHAT_ID.eq(tgChatId))
            .execute();
    }
}
