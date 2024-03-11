package edu.java.scrapper.clients;

import edu.java.scrapper.dto.github.RepositoryInfo;
import edu.java.scrapper.service.web.GitHubService;
import java.time.OffsetDateTime;
import java.util.List;
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
    public void getRepositoryInfo() {
        stubFor(get(urlEqualTo("/repos/owner/repo/activity"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToStr("src/test/resources/github/repository-info.json"))));

        List<RepositoryInfo> repositoryInfo = gitHubService.getRepositoryInfo("owner", "repo");

        assertNotNull(repositoryInfo);
        assertAll(
            () -> assertEquals(repositoryInfo.size(), 2),
            () -> assertEquals(751871695, repositoryInfo.getFirst().getId()),
            () -> assertEquals("hw5", repositoryInfo.getFirst().getRef()),
            () -> assertEquals(OffsetDateTime.parse("2024-03-10T22:40:35Z"), repositoryInfo.getFirst().getPushedAt()),
            () -> assertEquals("push", repositoryInfo.getFirst().getActivityType()),
            () -> assertEquals("Ilnur37", repositoryInfo.getFirst().getActor().getLogin())
        );
    }

    @Test
    public void getRepositoryInfo_404() {
        stubFor(get(urlEqualTo("/repos/owner/repo"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.NOT_FOUND.value())));

        assertThrows(
            WebClientResponseException.NotFound.class,
            () -> gitHubService.getRepositoryInfo("owner", "repo")
        );
    }
}
