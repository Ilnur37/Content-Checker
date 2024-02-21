package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class AbstractServiceTest {
    private WireMockServer wireMockServer;

    @BeforeEach
    public void setUp() {
        int port = 8080;
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(port));
        wireMockServer.start();
        WireMock.configureFor("localhost", port);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @SneakyThrows public String jsonToStr(String filepath) {
        return Files.readString(Paths.get(filepath));
    }
}
