package edu.java.configuration;

import edu.java.client.GithubClient;
import edu.java.client.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public GithubClient gitHubClient(ApplicationConfig appConf) {
        return GithubClient.create(appConf.api().gitUrl());
    }

    @Bean
    public StackOverflowClient stackOverflowClient(ApplicationConfig appConf) {
        return StackOverflowClient.create(appConf.api().stackoverflowUrl());
    }
}
