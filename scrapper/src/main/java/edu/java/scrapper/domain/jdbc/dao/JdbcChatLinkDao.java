package edu.java.scrapper.domain.jdbc.dao;

import edu.java.scrapper.domain.ChatLinkDao;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLink;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLinkRowMapper;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLinkWithTgChatRowMapper;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLinkWithUrlRowMapper;
import edu.java.scrapper.domain.model.ChatLinkWithTgChat;
import edu.java.scrapper.domain.model.ChatLinkWithUrl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;

@RequiredArgsConstructor
public class JdbcChatLinkDao implements ChatLinkDao<ChatLink> {

    private final JdbcClient jdbcClient;
    private final ChatLinkRowMapper chatLinkRowMapper;
    private final ChatLinkWithUrlRowMapper chatLinkWithUrlRowMapper;
    private final ChatLinkWithTgChatRowMapper chatLinkWithTgChatRowMapper;

    @Override
    public List<ChatLink> getAll() {
        String sql = "SELECT * FROM chat_link";
        return jdbcClient.sql(sql)
            .query(chatLinkRowMapper).list();
    }

    @Override
    public List<ChatLink> getByChatId(long id) {
        String sql = "SELECT * FROM chat_link WHERE chat_id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkRowMapper).list();
    }

    @Override
    public List<ChatLinkWithUrl> getByChatIdJoinLink(long id) {
        String sql = """
            SELECT cl.chat_id, cl.link_id, l.url
            FROM chat_link cl
            JOIN link l ON l.id = cl.link_id
            WHERE chat_id = ?""";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkWithUrlRowMapper).list();
    }

    @Override
    public List<ChatLink> getByLinkId(long id) {
        String sql = "SELECT * FROM chat_link WHERE link_id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkRowMapper).list();
    }

    @Override
    public List<ChatLinkWithTgChat> getByLinkIdJoinChat(long id) {
        String sql = """
            SELECT cl.chat_id, cl.link_id, c.tg_chat_id
            FROM chat_link cl
            JOIN chat c ON c.id = cl.chat_id
            WHERE link_id = ?""";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatLinkWithTgChatRowMapper).list();
    }

    @Override
    public int save(ChatLink chatLink) {
        String sql = "INSERT INTO chat_link(chat_id, link_id) VALUES (?, ?)";
        return jdbcClient.sql(sql)
            .params(chatLink.getChatId(), chatLink.getLinkId())
            .update();
    }

    @Override
    public int delete(long chatId, long linkId) {
        String sql = "DELETE FROM chat_link WHERE chat_id = ? AND link_id = ?";
        return jdbcClient.sql(sql)
            .params(chatId, linkId)
            .update();
    }
}
