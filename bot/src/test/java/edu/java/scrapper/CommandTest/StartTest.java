package edu.java.scrapper.CommandTest;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.handler.StartCommand;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StartTest extends AbstractTest {
    private static final String RESPONSE_USER_SUCCESSFULLY_REGISTERED = "Поздравляю, регистрация прошла успешно!";
    private static final String RESPONSE_USER_IS_ALREADY_REGISTERED = "Вы уже зарагестрированны";
    protected static final String USER_MUST_BE_REGISTERED =
        "Прежде чем пользоваться фцнкциями бота, вам необходимо зарегестрироваться. Введите команду \"/start\"";
    protected static final String UNSUPPORTED_COMMAND = "Пока что я не могу распознать это сообщение";
    @Autowired
    UserRepository userRepository;
    @Autowired
    StartCommand start;

    @Test
    @DisplayName("Корректные данные")
    void registrationUserWhenValidData() {
        Long chatId = new Random().nextLong();
        assertThat(userRepository.findUserById(chatId)).isEmpty();

        mockObjects(chatId, start.command());
        SendMessage response = start.handle(update);

        var newUser = userRepository.findUserById(chatId);
        assertThat(newUser).isPresent();
        assertAll(
            () -> assertThat(newUser.get().getId()).isEqualTo(chatId),
            () -> assertEquals(RESPONSE_USER_SUCCESSFULLY_REGISTERED, response.getParameters().get("text"))
        );
    }

    @Test
    @DisplayName("Повторная регистрация")
    void registrationUserWhenRepeatChatId() {
        Long chatId = new Random().nextLong();
        assertThat(userRepository.findUserById(chatId)).isEmpty();

        mockObjects(chatId, start.command());

        SendMessage response1 = start.handle(update);
        SendMessage response2 = start.handle(update);
        var newUser = userRepository.findUserById(chatId);

        assertThat(newUser).isPresent();
        assertAll(
            () -> assertThat(newUser.get().getId()).isEqualTo(chatId),
            () -> assertEquals(response1.getParameters().get("text"), RESPONSE_USER_SUCCESSFULLY_REGISTERED),
            () -> assertEquals(response2.getParameters().get("text"), RESPONSE_USER_IS_ALREADY_REGISTERED)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"12321", "pop", "1", "/star", "start", "qwerty"})
    @DisplayName("Ввод неподдерживаемой команды зарегестрированным поьзователем")
    void unsupportedCommandWhenUserRegistered(String command) {
        Long chatId = new Random().nextLong();
        userRepository.saveUser(new User(chatId));
        assertThat(userRepository.findUserById(chatId)).isPresent();

        mockObjects(chatId, command);

        SendMessage response = start.handle(update);
        assertEquals(UNSUPPORTED_COMMAND, response.getParameters().get("text"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12321", "pop", "1", "/star", "start", "qwerty"})
    @DisplayName("Ввод неподдерживаемой команды незарегестрированным поьзователем")
    void unsupportedCommandWhenUserNotRegistered(String command) {
        Long chatId = new Random().nextLong();

        mockObjects(chatId, command);
        SendMessage response = start.handle(update);
        var newUser = userRepository.findUserById(chatId);
        assertAll(
            () -> Assertions.assertTrue(newUser.isEmpty()),
            () -> assertEquals(USER_MUST_BE_REGISTERED, response.getParameters().get("text"))
        );
    }
}
