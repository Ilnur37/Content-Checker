package edu.java.scrapper.scheduler;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

public abstract class AbstractScheduler {
    static final Duration NEED_TO_CHECK = Duration.ofSeconds(10);
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm, dd.MM.yyyy");
    static final String GIT_HEAD = "В репозитории %s, пользователя %s, %d новых изменений:\n";
    static final String GIT_ABOUT = "В ветку %s были внесены изменения (тип: %s, время %s)\n";
    static final String SOF_HEAD = "В вопросе %s, пользователя %s появились новые ответы/комментарии:\n";
    static final String SOF_ANSWER = "Новый ответ от пользователя %s (время %s)\n";
    static final String SOF_COMMENT = "Новый комментарий от пользователя %s (время %s)\n";

}
