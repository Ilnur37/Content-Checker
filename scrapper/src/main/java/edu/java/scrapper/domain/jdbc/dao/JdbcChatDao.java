package edu.java.scrapper.domain.jdbc.dao;

import edu.java.scrapper.domain.ChatDao;
import edu.java.scrapper.domain.jdbc.model.chat.Chat;
import edu.java.scrapper.domain.jdbc.model.chat.ChatRowMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;

@RequiredArgsConstructor
public class JdbcChatDao implements ChatDao<Chat> {

    private final JdbcClient jdbcClient;
    private final ChatRowMapper chatRowMapper;

    @Override
    public List<Chat> getAll() {
        String sql = "SELECT * FROM chat";
        return jdbcClient.sql(sql)
            .query(chatRowMapper).list();
    }

    @Override
    public Optional<Chat> findByTgChatId(long tgChatId) {
        String sql = "SELECT * FROM chat WHERE tg_chat_id = ?";
        return jdbcClient.sql(sql)
            .param(tgChatId)
            .query(chatRowMapper).optional();
    }

    @Override
    public Optional<Chat> findById(long id) {
        String sql = "SELECT * FROM chat WHERE id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(chatRowMapper).optional();
    }

    @Override
    public int save(Chat chat) {
        String sql = "INSERT INTO chat(tg_chat_id, created_at) VALUES (?, ?)";
        return jdbcClient.sql(sql)
            .params(chat.getTgChatId(), chat.getCreatedAt())
            .update();
    }

    @Override
    public int delete(long tgChatId) {
        String sql = "DELETE FROM chat WHERE tg_chat_id = ?";
        return jdbcClient.sql(sql)
            .param(tgChatId)
            .update();
    }
}
