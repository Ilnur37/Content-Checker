package edu.java.scrapper.model.chatLink;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatLinkRowMapper implements RowMapper<ChatLink> {
    @Override
    @SuppressWarnings("MagicNumber")
    public ChatLink mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChatLink chatLink = new ChatLink();
        chatLink.setChatId(rs.getLong(1));
        chatLink.setLinkId(rs.getLong(2));
        return chatLink;
    }
}
