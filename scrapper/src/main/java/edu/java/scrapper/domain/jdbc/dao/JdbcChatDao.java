package edu.java.scrapper.domain.jdbc.dao;

import edu.java.scrapper.domain.jdbc.model.chat.Chat;
import edu.java.scrapper.domain.jdbc.model.chat.ChatRowMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatDao {
    private final JdbcClient jdbcClient;
    private final ChatRowMapper chatRowMapper;

    public List<Chat> getAll() {
        String sql = "SELECT * FROM chat";
        return jdbcClient.sql(sql)
            .query(chatRowMapper).list();
    }

    public Optional<Chat> getByTgChatId(long tgChatId) {
        String sql = "SELECT * FROM chat WHERE tg_chat_id = ?";
        return jdbcClient.sql(sql)
            .param(tgChatId)
            .query(chatRowMapper).optional();
    }

    public Optional<Chat> getById(long id) {
        String sql = "SELECT * FROM chat WHERE id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatRowMapper).optional();
    }

    public int save(Chat chat) {
        String sql = "INSERT INTO chat(tg_chat_id, created_at) VALUES (?, ?)";
        return jdbcClient.sql(sql)
            .params(chat.getTgChatId(), chat.getCreatedAt())
            .update();
    }

    public int delete(long tgChatId) {
        String sql = "DELETE FROM chat WHERE tg_chat_id = ?";
        return jdbcClient.sql(sql)
            .param(tgChatId)
            .update();
    }
}
