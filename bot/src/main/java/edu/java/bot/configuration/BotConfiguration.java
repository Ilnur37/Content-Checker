package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {
    @Bean
    TelegramBot telegramBot(ApplicationConfig appConfig) {
        return new TelegramBot(appConfig.telegramToken());
    }
}
