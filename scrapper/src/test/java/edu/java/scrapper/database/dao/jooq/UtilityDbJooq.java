package edu.java.scrapper.database.dao.jooq;

import edu.java.scrapper.domain.jooq.generate.tables.pojos.Chat;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.ChatLink;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.jooq.DSLContext;

@UtilityClass
public class UtilityDbJooq {
    private final edu.java.scrapper.domain.jooq.generate.tables.Chat chatTable =
        edu.java.scrapper.domain.jooq.generate.tables.Chat.CHAT;
    private final edu.java.scrapper.domain.jooq.generate.tables.Link linkTable =
        edu.java.scrapper.domain.jooq.generate.tables.Link.LINK;
    private final edu.java.scrapper.domain.jooq.generate.tables.ChatLink chatLinkTable =
        edu.java.scrapper.domain.jooq.generate.tables.ChatLink.CHAT_LINK;

    public static List<Chat> getAllFromChat(DSLContext dsl) {
        return dsl.selectFrom(chatTable)
            .fetchInto(Chat.class);
    }

    public static List<Link> getAllFromLink(DSLContext dsl) {
        return dsl.selectFrom(linkTable)
            .fetchInto(Link.class);
    }

    public static List<ChatLink> getAllFromChatLink(DSLContext dsl) {
        return dsl.selectFrom(chatLinkTable)
            .fetchInto(ChatLink.class);
    }

    public static Optional<Long> getIdFromLinkByUrl(DSLContext dsl, String url) {
        return dsl.select(linkTable.ID).from(linkTable)
            .where(linkTable.URL.eq(url))
            .fetchOptionalInto(Long.class);
    }

    public static Optional<Long> getIdFromChatByTgChatId(DSLContext dsl, long tgChatId) {
        return dsl.select(chatTable.ID).from(chatTable)
            .where(chatTable.TG_CHAT_ID.eq(tgChatId))
            .fetchOptionalInto(Long.class);
    }

    public static int insertRowIntoChat(DSLContext dsl, long tgChatId) {
        return dsl.insertInto(chatTable)
            .set(dsl.newRecord(chatTable, createChat(tgChatId)))
            .execute();
    }

    public static int insertRowIntoLink(DSLContext dsl, String url) {
        return dsl.insertInto(linkTable)
            .set(dsl.newRecord(linkTable, createLink(url)))
            .execute();
    }

    public static int insertRowIntoChatLink(DSLContext dsl, long chatId, long linkId) {
        return dsl.insertInto(chatLinkTable)
            .set(dsl.newRecord(chatLinkTable, createChatLink(chatId, linkId)))
            .execute();
    }

    public static Chat createChat(long tgChatId) {
        Chat chat = new Chat();
        chat.setTgChatId(tgChatId);
        chat.setCreatedAt(OffsetDateTime.now());
        return chat;
    }

    public static Link createLink(String url) {
        Link link = new Link();
        link.setUrl(url);
        link.setCreatedAt(OffsetDateTime.now());
        link.setLastUpdateAt(OffsetDateTime.now());
        link.setAuthor("Author");
        link.setName("Name");
        link.setLastCheckAt(OffsetDateTime.now());
        return link;
    }

    public static ChatLink createChatLink(long chatId, long linkId) {
        ChatLink chatLink = new ChatLink();
        chatLink.setChatId(chatId);
        chatLink.setLinkId(linkId);
        return chatLink;
    }
}
