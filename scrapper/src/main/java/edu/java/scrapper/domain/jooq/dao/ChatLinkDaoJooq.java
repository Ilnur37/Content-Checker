package edu.java.scrapper.domain.jooq.dao;

import edu.java.scrapper.domain.jooq.generate.tables.pojos.ChatLink;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatLinkDaoJooq {
    private final DSLContext dsl;
    private final edu.java.scrapper.domain.jooq.generate.tables.ChatLink chatLinkTable =
        edu.java.scrapper.domain.jooq.generate.tables.ChatLink.CHAT_LINK;

    public List<ChatLink> getAll() {
        return dsl.selectFrom(chatLinkTable)
            .fetchInto(ChatLink.class);
    }

    public Optional<ChatLink> getByChatId(long id) {
        return dsl.selectFrom(chatLinkTable)
            .where(chatLinkTable.CHAT_ID.eq(id))
            .fetchOptionalInto(ChatLink.class);
    }

    public Optional<ChatLink> getByLinkId(long id) {
        return dsl.selectFrom(chatLinkTable)
            .where(chatLinkTable.LINK_ID.eq(id))
            .fetchOptionalInto(ChatLink.class);
    }

    public int save(ChatLink newChatLink) {
        return dsl.insertInto(chatLinkTable)
            .set(dsl.newRecord(chatLinkTable, newChatLink))
            .execute();
    }

    public int delete(long chatId, long linkId) {
        return dsl.deleteFrom(chatLinkTable)
            .where(chatLinkTable.CHAT_ID.eq(chatId).and(chatLinkTable.LINK_ID.eq(linkId)))
            .execute();
    }
}
