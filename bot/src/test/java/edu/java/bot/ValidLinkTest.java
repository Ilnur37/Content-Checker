package edu.java.bot;

import edu.java.bot.domain.SupportedDomain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.mockito.Mockito.spy;

public class ValidLinkTest {
    private static final SupportedDomain supportedDomain = spy(SupportedDomain.class);

    @BeforeAll
    static void setUp() {
        supportedDomain.setGithub("github.com");
        supportedDomain.setStackoverflow("stackoverflow.com");
    }

    private static final String RESPONSE_COMMAND_SUPPORTS_ONE_PARAMETER =
        "Вы должны передать 1 ссылку с этой командой";
    private static final String RESPONSE_LINK_IS_INVALID =
        "Извините, пока что я не могу работать с ссылкой этого домена";

    @ParameterizedTest
    @ValueSource(strings = {"/track https://github.com/Ilnur37/Content-Checker",
        "/track https://github.com/Ilnur37/Tinkoff",
        "/track https://stackoverflow.com/questions/214741/what-is-a-stackoverflowerror"})
    @DisplayName("Корректные команды")
    void validLink(String link) {
        Assertions.assertTrue(supportedDomain.validateCommand(link.split(" ")).isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/track https://github.com/Ilnur37/Content-Checker/pull",
        "/track https://git.com/Ilnur37/Tinkoff",
        "/track https://stackoverflow.com/questions/214741"})
    @DisplayName("Некорректные ссылки")
    void invalidLink(String link) {
        Assertions.assertEquals(RESPONSE_LINK_IS_INVALID, supportedDomain.validateCommand(link.split(" ")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/track https://github.com/Ilnur37/Content-Checker/pull url",
        "/track",
        "/track https://stackoverflow.com/questions/214741 https://stackoverflow.com/questions/214741"})
    @DisplayName("Ноль или несколько ссылок")
    void invalidCountLink(String link) {
        Assertions.assertEquals(
            RESPONSE_COMMAND_SUPPORTS_ONE_PARAMETER,
            supportedDomain.validateCommand(link.split(" "))
        );
    }
}
