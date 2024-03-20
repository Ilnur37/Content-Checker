package edu.java.scrapper.domain.jdbc.model.chatLink;

import edu.java.scrapper.domain.model.ChatLinkWithTgChat;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ChatLinkWithTgChatRowMapper implements RowMapper<ChatLinkWithTgChat> {
    @Override
    @SuppressWarnings("MagicNumber")
    public ChatLinkWithTgChat mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChatLinkWithTgChat chatLinkWithTgChat = new ChatLinkWithTgChat();
        chatLinkWithTgChat.setChatId(rs.getLong(1));
        chatLinkWithTgChat.setLinkId(rs.getLong(2));
        chatLinkWithTgChat.setTgChatId(rs.getLong(3));
        return chatLinkWithTgChat;
    }
}
