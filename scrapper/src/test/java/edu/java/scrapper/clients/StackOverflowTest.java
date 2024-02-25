package edu.java.scrapper.clients;

import edu.java.scrapper.dto.stackoverflow.OwnerInfo;
import edu.java.scrapper.dto.stackoverflow.answer.AnswerInfo;
import edu.java.scrapper.dto.stackoverflow.question.QuestionInfo;
import edu.java.scrapper.service.StackOverflowService;
import java.util.List;
import lombok.SneakyThrows;
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

public class StackOverflowTest extends AbstractServiceTest {
    @Autowired
    private StackOverflowService stackOverflowService;

    @Test
    public void testGetAnswerInfoByQuestion() {
        stubFor(get(urlEqualTo("/questions/123/answers?order=desc&site=stackoverflow"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToStr("src/test/resources/stackoverflow/answer-info-by-question.json"))));

        List<AnswerInfo> answerInfo = stackOverflowService.getAnswerInfoByQuestion(123L);

        for (AnswerInfo info : answerInfo) {
            assertNotNull(info);
            OwnerInfo owner = info.getOwner();
            assertAll(
                "Owner info",
                () -> assertEquals(1000, owner.getAccountId()),
                () -> assertEquals(1002, owner.getUserId()),
                () -> assertEquals("tempUser", owner.getDisplayName()),
                () -> assertEquals("https://stackoverflow.com/users/1001/tempUser", owner.getLink())
            );
            assertAll(
                "Answer info",
                () -> assertEquals(1708162039, info.getLastActivityDate().toEpochSecond()),
                () -> assertEquals(1708162039, info.getCreationDate().toEpochSecond()),
                () -> assertEquals(1004, info.getAnswerId()),
                () -> assertEquals(123, info.getQuestionId())
            );
        }
    }

    @Test
    public void testGetQuestionInfo() {
        stubFor(get(urlEqualTo("/questions/123?order=desc&sort=activity&site=stackoverflow"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToStr("src/test/resources/stackoverflow/question-info.json"))));

        QuestionInfo questionInfo = stackOverflowService.getQuestionInfo(123L);

        assertNotNull(questionInfo);
        OwnerInfo owner = questionInfo.getOwner();
        assertAll(
            "Owner info",
            () -> assertEquals(2000, owner.getAccountId()),
            () -> assertEquals(2001, owner.getUserId()),
            () -> assertEquals("owner", owner.getDisplayName()),
            () -> assertEquals("https://stackoverflow.com/users/2001/owner", owner.getLink())
        );
        assertAll(
            "Question info",
            () -> assertEquals(1, questionInfo.getAnswerCount()),
            () -> assertEquals(1708162039, questionInfo.getLastActivityDate().toEpochSecond()),
            () -> assertEquals(1668289554, questionInfo.getCreationDate().toEpochSecond()),
            () -> assertEquals(123, questionInfo.getQuestionId())
        );
    }

    @Test
    public void testGetRepositoryInfo_404() {
        stubFor(get(urlEqualTo("/questions/123/answers?order=desc&site=stackoverflow"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.NOT_FOUND.value())));

        assertThrows(WebClientResponseException.NotFound.class, () -> {
            stackOverflowService.getQuestionInfo(123L);
        });
    }
}
