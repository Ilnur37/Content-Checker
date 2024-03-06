package edu.java.scrapper.database.dao;

import edu.java.scrapper.model.chat.Chat;
import edu.java.scrapper.model.chat.ChatRowMapper;
import edu.java.scrapper.model.chatLink.ChatLink;
import edu.java.scrapper.model.chatLink.ChatLinkRowMapper;
import edu.java.scrapper.model.link.Link;
import edu.java.scrapper.model.link.LinkRowMapper;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.simple.JdbcClient;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UtilityClass
public class UtilityDb {
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

    public static void checkThatTableLinkIsEmpty(JdbcClient jdbcClient) {
        List<Link> links = jdbcClient.sql(getAllFromLink)
            .query(linkRowMapper).list();
        assertTrue(links.isEmpty());
    }

    public static void checkThatTableChatLinkIsEmpty(JdbcClient jdbcClient) {
        List<ChatLink> content = jdbcClient.sql(getAllFromChatLink)
            .query(chatLinkRowMapper).list();
        assertTrue(content.isEmpty());
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
                "INSERT INTO link(url, created_at, last_update_at) VALUES (?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
            .param(url)
            .update();
    }
}
