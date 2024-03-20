package edu.java.scrapper.database;

import edu.java.scrapper.domain.jooq.dao.JooqChatDao;
import edu.java.scrapper.domain.jooq.dao.JooqChatLinkDao;
import edu.java.scrapper.domain.jooq.dao.JooqLinkDao;
import edu.java.scrapper.service.jooq.JooqChatService;
import edu.java.scrapper.service.jooq.JooqLinkService;
import edu.java.scrapper.service.web.WebResourceHandler;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class JooqIntegrationTest extends IntegrationTest {
    @Autowired
    private DSLContext dslContext;

    @Autowired
    private WebResourceHandler webResourceHandler;
    public JooqChatDao chatDao;
    public JooqLinkDao linkDao;
    public JooqChatLinkDao chatLinkDao;
    public JooqChatService chatService;
    public JooqLinkService linkService;

    @BeforeEach
    public void setUp() {
        chatDao = new JooqChatDao(dslContext);

        linkDao = new JooqLinkDao(dslContext);

        chatLinkDao = new JooqChatLinkDao(dslContext);

        chatService = new JooqChatService(chatDao);

        linkService = new JooqLinkService(chatDao, linkDao, chatLinkDao, webResourceHandler);
    }
}
