package edu.java.scrapper.domain.jdbc.model.chat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import org.springframework.jdbc.core.RowMapper;

public class ChatRowMapper implements RowMapper<Chat> {
    @Override
    @SuppressWarnings("MagicNumber")
    public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
        Chat chat = new Chat();
        chat.setId(rs.getLong(1));
        chat.setTgChatId(rs.getLong(2));
        chat.setCreatedAt(rs.getObject(3, OffsetDateTime.class));
        return chat;
    }
}
