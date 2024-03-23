package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import java.nio.file.Files;
import java.nio.file.Paths;
import edu.java.scrapper.database.IntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AbstractServiceTest extends IntegrationTest {
    private WireMockServer wireMockServer;
    private final int port = 8080;
    private final String host = "localhost";

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(port));
        wireMockServer.start();
        WireMock.configureFor(host, port);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @SneakyThrows public String jsonToStr(String filepath) {
        return Files.readString(Paths.get(filepath));
    }
}
