package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.jooq.dao.JooqChatDao;
import edu.java.scrapper.domain.jooq.dao.JooqChatLinkDao;
import edu.java.scrapper.domain.jooq.dao.JooqLinkDao;
import edu.java.scrapper.scheduler.JooqLinkUpdaterScheduler;
import edu.java.scrapper.service.BotService;
import edu.java.scrapper.service.jooq.JooqChatService;
import edu.java.scrapper.service.jooq.JooqLinkService;
import edu.java.scrapper.service.web.WebResourceHandler;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfig {

    @Bean
    public JooqChatDao jooqChatDao(DSLContext dslContext) {
        return new JooqChatDao(dslContext);
    }

    @Bean
    public JooqLinkDao jooqLinkDao(DSLContext dslContext) {
        return new JooqLinkDao(dslContext);
    }

    @Bean
    public JooqChatLinkDao jooqChatLinkDao(DSLContext dslContext) {
        return new JooqChatLinkDao(dslContext);
    }

    @Bean
    public JooqChatService jooqChatService(JooqChatDao jooqChatDao) {
        return new JooqChatService(jooqChatDao);
    }

    @Bean
    public JooqLinkService jooqLinkService(
        JooqChatDao chatDao,
        JooqLinkDao linkDao,
        JooqChatLinkDao chatLinkDao,
        WebResourceHandler webResourceHandler
    ) {
        return new JooqLinkService(
            chatDao,
            linkDao,
            chatLinkDao,
            webResourceHandler
        );
    }

    @Bean
    public JooqLinkUpdaterScheduler jooqLinkUpdaterScheduler(
        JooqLinkDao linkDao,
        JooqChatLinkDao chatLinkDao,
        BotService botService,
        WebResourceHandler webResourceHandler
    ) {
        return new JooqLinkUpdaterScheduler(
            linkDao,
            chatLinkDao,
            botService,
            webResourceHandler
        );
    }
}
