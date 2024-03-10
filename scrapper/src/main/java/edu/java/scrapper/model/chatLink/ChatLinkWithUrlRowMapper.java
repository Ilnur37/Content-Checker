package edu.java.scrapper.model.chatLink;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatLinkWithUrlRowMapper implements RowMapper<ChatLinkWithUrl> {
    @Override
    @SuppressWarnings("MagicNumber")
    public ChatLinkWithUrl mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChatLinkWithUrl chatLinkWithUrl = new ChatLinkWithUrl();
        chatLinkWithUrl.setChatId(rs.getLong(1));
        chatLinkWithUrl.setLinkId(rs.getLong(2));
        chatLinkWithUrl.setUrl(rs.getString(3));
        return chatLinkWithUrl;
    }
}
