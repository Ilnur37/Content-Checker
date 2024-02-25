package edu.java.configuration;

<<<<<<< HEAD
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
=======
import edu.java.client.GithubClient;
import edu.java.client.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
>>>>>>> hw2

@Configuration
public class ClientConfig {

    @Bean
<<<<<<< HEAD
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

    @Bean
    public WebClient botClient(ApplicationConfig appConf) {
        return WebClient.builder()
            .baseUrl(appConf.botUrl())
            .build();
=======
    public GithubClient gitHubClient(ApplicationConfig appConf) {
        return GithubClient.create(appConf.api().gitUrl());
    }

    @Bean
    public StackOverflowClient stackOverflowClient(ApplicationConfig appConf) {
        return StackOverflowClient.create(appConf.api().stackoverflowUrl());
>>>>>>> hw2
    }
}
