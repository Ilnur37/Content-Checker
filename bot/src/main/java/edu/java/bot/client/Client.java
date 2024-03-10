package edu.java.bot.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public abstract class Client {
    final WebClient webClient;
}
