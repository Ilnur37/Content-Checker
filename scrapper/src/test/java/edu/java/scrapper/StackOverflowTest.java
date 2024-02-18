package edu.java.scrapper;

import edu.java.dto.stackoverflow.AnswerInfo;
import edu.java.dto.stackoverflow.QuestionInfo;
import edu.java.service.StackOverflowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest("app.stackoverflow-url=http://localhost:8080")
public class StackOverflowTest extends AbstractServiceTest {
    @Autowired
    private StackOverflowService stackOverflowService;

    @Test
    public void testGetRepositoryInfo() {
        stubFor(get(urlEqualTo("/questions/123/answers?order=desc&site=stackoverflow"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody("{\n" +
                    "    \"items\": [\n" +
                    "        {\n" +
                    "            \"owner\": {\n" +
                    "                \"account_id\": 12808457,\n" +
                    "                \"user_id\": 9267406,\n" +
                    "                \"display_name\": \"imhvost\",\n" +
                    "                \"link\": \"https://stackoverflow.com/users/9267406/imhvost\"\n" +
                    "            },\n" +
                    "            \"creation_date\": 1708162039,\n" +
                    "            \"answer_id\": 78011584,\n" +
                    "            \"question_id\": 74416834\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}")));

        QuestionInfo questionInfo = stackOverflowService.getQuestionInfo(123L);

        assertNotNull(questionInfo);
        assertAll(
            "QuestionInfo assertions",
            () -> assertEquals(1, questionInfo.getItems().size()),
            () -> {
                AnswerInfo answerInfo = questionInfo.getItems().getFirst();
                assertNotNull(answerInfo);
                assertAll(
                    "AnswerInfo assertions",
                    () -> assertEquals(12808457, answerInfo.getOwner().getAccountId()),
                    () -> assertEquals(9267406, answerInfo.getOwner().getUserId()),
                    () -> assertEquals("imhvost", answerInfo.getOwner().getDisplayName()),
                    () -> assertEquals(
                        "https://stackoverflow.com/users/9267406/imhvost",
                        answerInfo.getOwner().getLink()
                    ),
                    () -> assertEquals(1708162039, answerInfo.getCreationDate().toEpochSecond()),
                    () -> assertEquals(78011584, answerInfo.getAnswerId()),
                    () -> assertEquals(74416834, answerInfo.getQuestionId())
                );
            }
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
