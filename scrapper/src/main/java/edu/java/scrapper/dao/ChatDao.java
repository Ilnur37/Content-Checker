package edu.java.scrapper.dao;

import edu.java.scrapper.model.chat.Chat;
import edu.java.scrapper.model.chat.ChatRowMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatDao implements Dao<Chat> {
    public final JdbcClient jdbcClient;
    private final ChatRowMapper chatRowMapper;

    public Optional<Chat> getByTgChatId(long id) {
        String sql = "SELECT * FROM chat WHERE tg_chat_id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatRowMapper).optional();
    }

    public Optional<Chat> getById(long id) {
        String sql = "SELECT * FROM chat WHERE id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatRowMapper).optional();
    }

    @Override
    public List<Chat> getAll() {
        String sql = "SELECT * FROM chat";
        return jdbcClient.sql(sql)
            .query(chatRowMapper).list();
    }

    @Override
    public int save(Chat chat) {
        String sql = "INSERT INTO chat(tg_chat_id, created_at) VALUES (?, ?)";
        return jdbcClient.sql(sql)
            .params(chat.getTgChatId(), chat.getCreatedAt())
            .update();
    }

    @Override
    public int delete(Chat chat) {
        String sql = "DELETE FROM chat WHERE tg_chat_id = ?";
        return jdbcClient.sql(sql)
            .param(chat.getTgChatId())
            .update();
    }
}
