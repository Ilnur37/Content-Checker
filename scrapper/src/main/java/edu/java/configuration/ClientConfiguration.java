package edu.java.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    @Bean
    public WebClient gitHubClient(ApplicationConfig appConf) {
        return WebClient.builder()
            .baseUrl(appConf.gitUrl())
            .build();
    }

    @Bean
    public WebClient stackOverflowClient(ApplicationConfig appConf) {
        return WebClient.builder()
            .baseUrl(appConf.stackoverflowUrl())
            .build();
    }
}
