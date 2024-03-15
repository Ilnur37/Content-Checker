package edu.java.scrapper.domain.jooq.dao;

import edu.java.scrapper.domain.jooq.generate.tables.pojos.ChatLink;
import edu.java.scrapper.domain.model.ChatLinkWithTgChat;
import edu.java.scrapper.domain.model.ChatLinkWithUrl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JooqChatLinkDao {
    private final DSLContext dsl;

    private final edu.java.scrapper.domain.jooq.generate.tables.Chat chatTable =
        edu.java.scrapper.domain.jooq.generate.tables.Chat.CHAT;
    private final edu.java.scrapper.domain.jooq.generate.tables.Link linkTable =
        edu.java.scrapper.domain.jooq.generate.tables.Link.LINK;
    private final edu.java.scrapper.domain.jooq.generate.tables.ChatLink chatLinkTable =
        edu.java.scrapper.domain.jooq.generate.tables.ChatLink.CHAT_LINK;

    public List<ChatLink> getAll() {
        return dsl.selectFrom(chatLinkTable)
            .fetchInto(ChatLink.class);
    }

    public List<ChatLink> getByChatId(long id) {
        return dsl.selectFrom(chatLinkTable)
            .where(chatLinkTable.CHAT_ID.eq(id))
            .fetchInto(ChatLink.class);
    }

    public List<ChatLinkWithUrl> getByChatIdJoinLink(long id) {
        return dsl.select(chatLinkTable.CHAT_ID, chatLinkTable.LINK_ID, linkTable.URL)
            .from(chatLinkTable)
            .join(linkTable).on(chatLinkTable.LINK_ID.eq(linkTable.ID))
            .where(chatLinkTable.CHAT_ID.eq(id))
            .fetchInto(ChatLinkWithUrl.class);
    }

    public List<ChatLink> getByLinkId(long id) {
        return dsl.selectFrom(chatLinkTable)
            .where(chatLinkTable.LINK_ID.eq(id))
            .fetchInto(ChatLink.class);
    }

    public List<ChatLinkWithTgChat> getByLinkIdIdJoinChat(long id) {
        return dsl.select(chatLinkTable.CHAT_ID, chatLinkTable.LINK_ID, chatTable.TG_CHAT_ID)
            .from(chatLinkTable)
            .join(chatTable).on(chatLinkTable.CHAT_ID.eq(chatTable.ID))
            .where(chatLinkTable.CHAT_ID.eq(id))
            .fetchInto(ChatLinkWithTgChat.class);
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
