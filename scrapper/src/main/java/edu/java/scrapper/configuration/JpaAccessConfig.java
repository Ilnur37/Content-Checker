package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.jpa.dao.JpaChatRepository;
import edu.java.scrapper.domain.jpa.dao.JpaLinkRepository;
import edu.java.scrapper.scheduler.JpaLinkUpdaterScheduler;
import edu.java.scrapper.service.BotService;
import edu.java.scrapper.service.jpa.JpaChatService;
import edu.java.scrapper.service.jpa.JpaLinkService;
import edu.java.scrapper.service.web.WebResourceHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfig {

    @Bean
    public JpaChatService jpaChatService(JpaChatRepository jpaChatRepository) {
        return new JpaChatService(jpaChatRepository);
    }

    @Bean
    public JpaLinkService jpaLinkService(
        JpaChatRepository jpaChatRepository,
        JpaLinkRepository jpaLinkRepository,
        WebResourceHandler webResourceHandler
    ) {
        return new JpaLinkService(
            jpaChatRepository,
            jpaLinkRepository,
            webResourceHandler
        );
    }

    @Bean
    public JpaLinkUpdaterScheduler jpaLinkUpdaterScheduler(
        JpaLinkRepository jpaLinkRepository,
        BotService botService,
        WebResourceHandler webResourceHandler
    ) {
        return new JpaLinkUpdaterScheduler(
            jpaLinkRepository,
            botService,
            webResourceHandler
        );
    }
}
