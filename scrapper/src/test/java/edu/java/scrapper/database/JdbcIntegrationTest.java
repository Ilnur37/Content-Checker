package edu.java.scrapper.database;

import edu.java.scrapper.domain.jdbc.dao.JdbcChatDao;
import edu.java.scrapper.domain.jdbc.dao.JdbcChatLinkDao;
import edu.java.scrapper.domain.jdbc.dao.JdbcLinkDao;
import edu.java.scrapper.domain.jdbc.model.chat.ChatRowMapper;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLinkRowMapper;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLinkWithTgChatRowMapper;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLinkWithUrlRowMapper;
import edu.java.scrapper.domain.jdbc.model.link.LinkRowMapper;
import edu.java.scrapper.service.jdbc.JdbcChatService;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import edu.java.scrapper.service.web.WebResourceHandler;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

public class JdbcIntegrationTest extends IntegrationTest {

    @Autowired
    public JdbcClient jdbcClient;

    @Autowired
    private WebResourceHandler webResourceHandler;

    public JdbcChatDao chatDao;
    public JdbcLinkDao linkDao;
    public JdbcChatLinkDao chatLinkDao;
    public JdbcChatService chatService;
    public JdbcLinkService linkService;

    @BeforeEach
    public void setUp() {
        chatDao = new JdbcChatDao(this.jdbcClient, new ChatRowMapper());

        linkDao = new JdbcLinkDao(this.jdbcClient, new LinkRowMapper());

        chatLinkDao = new JdbcChatLinkDao(
            this.jdbcClient,
            new ChatLinkRowMapper(),
            new ChatLinkWithUrlRowMapper(),
            new ChatLinkWithTgChatRowMapper()
        );

        chatService = new JdbcChatService(chatDao);

        linkService = new JdbcLinkService(chatDao, linkDao, chatLinkDao, webResourceHandler);
    }
}
