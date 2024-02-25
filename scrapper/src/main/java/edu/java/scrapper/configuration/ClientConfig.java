package edu.java.scrapper.configuration;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.client.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public BotClient botClient(ApplicationConfig appConf) {
        return BotClient.create(appConf.api().botUrl());
    }

    @Bean
    public GithubClient gitHubClient(ApplicationConfig appConf) {
        return GithubClient.create(appConf.api().gitUrl());
    }

    @Bean
    public StackOverflowClient stackOverflowClient(ApplicationConfig appConf) {
        return StackOverflowClient.create(appConf.api().stackoverflowUrl());
    }
}
