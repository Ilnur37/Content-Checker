package edu.java.scrapper.domain.jooq.dao;

import edu.java.scrapper.domain.ChatLinkDao;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.ChatLink;
import edu.java.scrapper.domain.model.ChatLinkWithTgChat;
import edu.java.scrapper.domain.model.ChatLinkWithUrl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.scrapper.domain.jooq.generate.tables.Chat.CHAT;
import static edu.java.scrapper.domain.jooq.generate.tables.ChatLink.CHAT_LINK;
import static edu.java.scrapper.domain.jooq.generate.tables.Link.LINK;

@RequiredArgsConstructor
public class JooqChatLinkDao implements ChatLinkDao<ChatLink> {

    private final DSLContext dsl;

    @Override
    public List<ChatLink> getAll() {
        return dsl.selectFrom(CHAT_LINK)
            .fetchInto(ChatLink.class);
    }

    @Override
    public List<ChatLink> getByChatId(long id) {
        return dsl.selectFrom(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.eq(id))
            .fetchInto(ChatLink.class);
    }

    @Override
    public List<ChatLinkWithUrl> getByChatIdJoinLink(long id) {
        return dsl.select(CHAT_LINK.CHAT_ID, CHAT_LINK.LINK_ID, LINK.URL)
            .from(CHAT_LINK)
            .join(LINK).on(CHAT_LINK.LINK_ID.eq(LINK.ID))
            .where(CHAT_LINK.CHAT_ID.eq(id))
            .fetchInto(ChatLinkWithUrl.class);
    }

    @Override
    public List<ChatLink> getByLinkId(long id) {
        return dsl.selectFrom(CHAT_LINK)
            .where(CHAT_LINK.LINK_ID.eq(id))
            .fetchInto(ChatLink.class);
    }

    @Override
    public List<ChatLinkWithTgChat> getByLinkIdJoinChat(long id) {
        return dsl.select(CHAT_LINK.CHAT_ID, CHAT_LINK.LINK_ID, CHAT.TG_CHAT_ID)
            .from(CHAT_LINK)
            .join(CHAT).on(CHAT_LINK.CHAT_ID.eq(CHAT.ID))
            .where(CHAT_LINK.LINK_ID.eq(id))
            .fetchInto(ChatLinkWithTgChat.class);
    }

    @Override
    public int save(ChatLink newChatLink) {
        return dsl.insertInto(CHAT_LINK)
            .set(dsl.newRecord(CHAT_LINK, newChatLink))
            .execute();
    }

    @Override
    public int delete(long chatId, long linkId) {
        return dsl.deleteFrom(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.eq(chatId).and(CHAT_LINK.LINK_ID.eq(linkId)))
            .execute();
    }
}
