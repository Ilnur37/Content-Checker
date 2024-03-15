package edu.java.scrapper.database.dao.jdbc;

import edu.java.scrapper.domain.jdbc.model.chat.Chat;
import edu.java.scrapper.domain.jdbc.model.chat.ChatRowMapper;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLink;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLinkRowMapper;
import edu.java.scrapper.domain.jdbc.model.link.Link;
import edu.java.scrapper.domain.jdbc.model.link.LinkRowMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.simple.JdbcClient;

@UtilityClass
public class UtilityDbJdbc {
    private final ChatRowMapper chatRowMapper = new ChatRowMapper();
    private final LinkRowMapper linkRowMapper = new LinkRowMapper();
    private final ChatLinkRowMapper chatLinkRowMapper = new ChatLinkRowMapper();
    private final String getAllFromChat = "SELECT * FROM chat";
    private final String getAllFromLink = "SELECT * FROM link";
    private final String getAllFromChatLink = "SELECT * FROM chat_link";

    public static List<Chat> getAllFromChat(JdbcClient jdbcClient) {
        return jdbcClient.sql(getAllFromChat)
            .query(chatRowMapper)
            .list();
    }

    public static List<Link> getAllFromLink(JdbcClient jdbcClient) {
        return jdbcClient.sql(getAllFromLink)
            .query(linkRowMapper)
            .list();
    }

    public static List<ChatLink> getAllFromChatLink(JdbcClient jdbcClient) {
        return jdbcClient.sql(getAllFromChatLink)
            .query(chatLinkRowMapper)
            .list();
    }

    public static Optional<Long> getIdFromChatByTgChatId(JdbcClient jdbcClient, long tgChatId) {
        return jdbcClient.sql("SELECT (id) FROM chat WHERE tg_chat_id = ?")
            .param(tgChatId)
            .query(Long.class).optional();
    }

    public static Optional<Long> getIdFromLinkByUrl(JdbcClient jdbcClient, String url) {
        return jdbcClient.sql("SELECT (id) FROM link WHERE url = ?")
            .param(url)
            .query(Long.class).optional();
    }

    public static void insertRowIntoChat(JdbcClient jdbcClient, long tgChatId) {
        jdbcClient.sql("INSERT INTO chat(tg_chat_id, created_at) VALUES (?, CURRENT_TIMESTAMP)")
            .param(tgChatId)
            .update();
    }

    public static void insertRowIntoLink(JdbcClient jdbcClient, String url) {
        jdbcClient.sql(
                "INSERT INTO link(url, created_at, last_update_at, last_check_at) " +
                    "VALUES (?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
            .param(url)
            .update();
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
