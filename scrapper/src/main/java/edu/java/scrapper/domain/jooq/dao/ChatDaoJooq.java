package edu.java.scrapper.domain.jooq.dao;

import edu.java.scrapper.domain.jooq.generate.tables.pojos.Chat;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatDaoJooq {
    private final DSLContext dsl;
    private final edu.java.scrapper.domain.jooq.generate.tables.Chat chatTable =
        edu.java.scrapper.domain.jooq.generate.tables.Chat.CHAT;

    public List<Chat> getAll() {
        return dsl.selectFrom(chatTable)
            .fetchInto(Chat.class);
    }

    public Optional<Chat> getByTgChatId(Long tgChatId) {
        return dsl.selectFrom(chatTable)
            .where(chatTable.TG_CHAT_ID.eq(tgChatId))
            .fetchOptionalInto(Chat.class);
    }

    public Optional<Chat> getById(Long id) {
        return dsl.selectFrom(chatTable)
            .where(chatTable.ID.eq(id))
            .fetchOptionalInto(Chat.class);
    }

    public int save(Chat newChat) {
        return dsl.insertInto(chatTable)
            .set(dsl.newRecord(chatTable, newChat))
            .execute();
    }

    public int delete(long tgChatId) {
        return dsl.deleteFrom(chatTable)
            .where(chatTable.TG_CHAT_ID.eq(tgChatId))
            .execute();
    }
}
