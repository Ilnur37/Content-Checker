package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.handler.StartCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {BotApplication.class})
public class ParseLinkTest {
    @MockBean
    Update update;
    @Autowired
    StartCommand start;

    /*@Test
    @DisplayName("Преобразование ссылки в Link")
    void parseLink() {
        String link = "https://github.com/Ilnur37/Content-Checker";
        Link actualLink = start.parsePartOfMessageIntoLink(link);
        Link expectedLink = new Link(new Domain("https://github.com"), "/Ilnur37/Content-Checker");
        Assertions.assertEquals(expectedLink, actualLink);
    }*/
}
