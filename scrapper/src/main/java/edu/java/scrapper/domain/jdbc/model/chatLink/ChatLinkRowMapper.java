package edu.java.scrapper.domain.jdbc.model.chatLink;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ChatLinkRowMapper implements RowMapper<ChatLink> {
    @Override
    @SuppressWarnings("MagicNumber")
    public ChatLink mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChatLink chatLink = new ChatLink();
        chatLink.setId(rs.getLong(1));
        chatLink.setChatId(rs.getLong(2));
        chatLink.setLinkId(rs.getLong(3));
        return chatLink;
    }
}
