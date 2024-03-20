package edu.java.scrapper.domain.jdbc.model.chatLink;

import edu.java.scrapper.domain.model.ChatLinkWithUrl;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

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
