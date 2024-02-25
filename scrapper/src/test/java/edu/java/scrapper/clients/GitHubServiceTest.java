package edu.java.scrapper.clients;

import edu.java.dto.github.RepositoryInfo;
import edu.java.service.GitHubService;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GitHubServiceTest extends AbstractServiceTest {
    @Autowired
    private GitHubService gitHubService;

    @Test
    public void testGetRepositoryInfo() {
        stubFor(get(urlEqualTo("/repos/owner/repo"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToStr("src/test/resources/github/repository-info.json"))));

        RepositoryInfo repositoryInfo = gitHubService.getRepositoryInfo("owner", "repo");

        assertNotNull(repositoryInfo);
        assertAll(
            () -> assertEquals(751871695, repositoryInfo.getId()),
            () -> assertEquals("Ilnur37/Content-Checker", repositoryInfo.getFullName()),
            () -> assertEquals("https://api.github.com/repos/Ilnur37/Content-Checker", repositoryInfo.getUrl()),
            () -> assertEquals(OffsetDateTime.parse("2024-02-16T18:02:44Z"), repositoryInfo.getPushedAt())
        );
    }

    @Test
    public void testGetRepositoryInfo_404() {
        stubFor(get(urlEqualTo("/repos/owner/repo"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.NOT_FOUND.value())));

        assertThrows(WebClientResponseException.NotFound.class, () -> {
            gitHubService.getRepositoryInfo("owner", "repo");
        });
    }
}
