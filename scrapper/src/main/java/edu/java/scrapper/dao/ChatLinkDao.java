package edu.java.scrapper.dao;

import edu.java.scrapper.model.chatLink.ChatLink;
import edu.java.scrapper.model.chatLink.ChatLinkRowMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatLinkDao {
    private final JdbcClient jdbcClient;
    private final ChatLinkRowMapper chatLinkRowMapper;

    public List<ChatLink> getByChatId(long id) {
        String sql = "SELECT * FROM chat_link WHERE chat_id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkRowMapper).list();
    }

    public List<ChatLink> getByLinkId(long id) {
        String sql = "SELECT * FROM chat_link WHERE link_id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkRowMapper).list();
    }

    public List<ChatLink> getAll() {
        String sql = "SELECT * FROM chat_link";
        return jdbcClient.sql(sql)
            .query(chatLinkRowMapper).list();
    }

    public int save(ChatLink chatLink) {
        String sql = "INSERT INTO chat_link(chat_id, link_id) VALUES (?, ?)";
        return jdbcClient.sql(sql)
            .params(chatLink.getChatId(), chatLink.getLinkId())
            .update();
    }

    public int delete(long chatId, long linkId) {
        String sql = "DELETE FROM chat_link WHERE chat_id = ? AND link_id = ?";
        return jdbcClient.sql(sql)
            .params(chatId, linkId)
            .update();
    }
}
