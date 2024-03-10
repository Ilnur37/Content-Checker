package edu.java.bot;

import edu.java.bot.domain.SupportedDomain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {BotApplication.class})
public class ValidLinkTest {
    @Autowired
    SupportedDomain supportedDomain;

    @ParameterizedTest
    @ValueSource(strings = {"https://github.com/Ilnur37/Content-Checker",
        "https://github.com/Ilnur37/Tinkoff",
        "https://stackoverflow.com/questions/214741/what-is-a-stackoverflowerror"})
    void validLink(String link) {
        Assertions.assertTrue(supportedDomain.isValid(link));
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://github.com/Ilnur37/Content-Checker/pull",
        "https://git.com/Ilnur37/Tinkoff",
        "https://stackoverflow.com/questions/214741"})
    void invalidLink(String link) {
        Assertions.assertFalse(supportedDomain.isValid(link));
    }
}
