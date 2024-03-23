package edu.java.scrapper.database;

import edu.java.scrapper.domain.jpa.dao.JpaChatRepository;
import edu.java.scrapper.domain.jpa.dao.JpaLinkRepository;
import edu.java.scrapper.service.jpa.JpaChatService;
import edu.java.scrapper.service.jpa.JpaLinkService;
import edu.java.scrapper.service.web.WebResourceHandler;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class JpaIntegrationTest extends IntegrationTest {

    @Autowired
    private WebResourceHandler webResourceHandler;

    @Autowired
    public JpaChatRepository chatRepository;

    @Autowired
    public JpaLinkRepository linkRepository;

    public JpaChatService chatService;
    public JpaLinkService linkService;

    @BeforeEach
    public void setUp() {
        chatService = new JpaChatService(chatRepository);

        linkService = new JpaLinkService(
            chatRepository,
            linkRepository,
            webResourceHandler
        );
    }
}
