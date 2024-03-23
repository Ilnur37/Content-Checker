package edu.java.scrapper.domain.jooq.dao;

import edu.java.scrapper.domain.ChatDao;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.Chat;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

@RequiredArgsConstructor
public class JooqChatDao implements ChatDao<Chat> {

    private final DSLContext dsl;

    private final edu.java.scrapper.domain.jooq.generate.tables.Chat chatTable =
        edu.java.scrapper.domain.jooq.generate.tables.Chat.CHAT;

    @Override
    public List<Chat> getAll() {
        return dsl.selectFrom(chatTable)
            .fetchInto(Chat.class);
    }

    @Override
    public Optional<Chat> findByTgChatId(long tgChatId) {
        return dsl.selectFrom(chatTable)
            .where(chatTable.TG_CHAT_ID.eq(tgChatId))
            .fetchOptionalInto(Chat.class);
    }

    @Override
    public Optional<Chat> findById(long id) {
        return dsl.selectFrom(chatTable)
            .where(chatTable.ID.eq(id))
            .fetchOptionalInto(Chat.class);
    }

    @Override
    public int save(Chat newChat) {
        return dsl.insertInto(chatTable)
            .set(dsl.newRecord(chatTable, newChat))
            .execute();
    }

    @Override
    public int delete(long tgChatId) {
        return dsl.deleteFrom(chatTable)
            .where(chatTable.TG_CHAT_ID.eq(tgChatId))
            .execute();
    }
}
