package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.jdbc.dao.JdbcChatDao;
import edu.java.scrapper.domain.jdbc.dao.JdbcChatLinkDao;
import edu.java.scrapper.domain.jdbc.dao.JdbcLinkDao;
import edu.java.scrapper.domain.jdbc.model.chat.ChatRowMapper;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLinkRowMapper;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLinkWithTgChatRowMapper;
import edu.java.scrapper.domain.jdbc.model.chatLink.ChatLinkWithUrlRowMapper;
import edu.java.scrapper.domain.jdbc.model.link.LinkRowMapper;
import edu.java.scrapper.scheduler.JdbcLinkUpdaterScheduler;
import edu.java.scrapper.service.BotService;
import edu.java.scrapper.service.jdbc.JdbcChatService;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import edu.java.scrapper.service.web.WebResourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfig {

    @Autowired
    private JdbcClient jdbcClient;

    @Bean
    public ChatRowMapper chatRowMapper() {
        return new ChatRowMapper();
    }

    @Bean
    public LinkRowMapper linkRowMapper() {
        return new LinkRowMapper();
    }

    @Bean
    public ChatLinkRowMapper chatLinkRowMapper() {
        return new ChatLinkRowMapper();
    }

    @Bean
    public ChatLinkWithUrlRowMapper chatLinkWithUrlRowMapper() {
        return new ChatLinkWithUrlRowMapper();
    }

    @Bean
    public ChatLinkWithTgChatRowMapper chatLinkWithTgChatRowMapper() {
        return new ChatLinkWithTgChatRowMapper();
    }

    @Bean
    public JdbcChatDao jdbcChatDao(ChatRowMapper chatRowMapper) {
        return new JdbcChatDao(jdbcClient, chatRowMapper);
    }

    @Bean
    public JdbcLinkDao jdbcLinkDao(LinkRowMapper linkRowMapper) {
        return new JdbcLinkDao(jdbcClient, linkRowMapper);
    }

    @Bean
    public JdbcChatLinkDao jdbcChatLinkDao(
        ChatLinkRowMapper chatLinkRowMapper,
        ChatLinkWithUrlRowMapper chatLinkWithUrlRowMapper,
        ChatLinkWithTgChatRowMapper chatLinkWithTgChatRowMapper
    ) {
        return new JdbcChatLinkDao(
            jdbcClient,
            chatLinkRowMapper,
            chatLinkWithUrlRowMapper,
            chatLinkWithTgChatRowMapper
        );
    }

    @Bean
    public JdbcChatService jdbcChatService(JdbcChatDao chatDao) {
        return new JdbcChatService(chatDao);
    }

    @Bean
    public JdbcLinkService jdbcLinkService(
        JdbcChatDao chatDao,
        JdbcLinkDao linkDao,
        JdbcChatLinkDao chatLinkDao,
        WebResourceHandler webResourceHandler
    ) {
        return new JdbcLinkService(chatDao, linkDao, chatLinkDao, webResourceHandler);
    }

    @Bean
    public JdbcLinkUpdaterScheduler jdbcLinkUpdaterScheduler(
        JdbcLinkDao linkDao,
        JdbcChatLinkDao chatLinkDao,
        BotService botService,
        WebResourceHandler webResourceHandler
    ) {
        return new JdbcLinkUpdaterScheduler(
            linkDao,
            chatLinkDao,
            botService,
            webResourceHandler
        );
    }
}
