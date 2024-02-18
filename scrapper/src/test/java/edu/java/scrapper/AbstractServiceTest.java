package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
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
}
