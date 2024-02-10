package CommandTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotApplication;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.handler.StartCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BotApplication.class})
public class StartTest {
    private static final String RESPONSE_USER_SUCCESSFULLY_REGISTERED = "Поздравляю, регистрация прошла успешно!";
    private static final String RESPONSE_USER_IS_ALREADY_REGISTERED = "Вы уже зарагестрированны";
    protected static final String USER_MUST_BE_REGISTERED =
        "Прежде чем пользоваться фцнкциями бота, вам необходимо зарегестрироваться. Введите команду \"/start\"";
    @MockBean
    Update update;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StartCommand start;

    @Test
    @DisplayName("Регистрация пользователя (корректные данные)")
    void registerUser_validData() {
        var chatId = 1L;
        assertThat(userRepository.findUserById(chatId)).isEmpty();

        Message mockMessage = mock(Message.class);
        Chat mockChat = mock(Chat.class);
        when(update.message()).thenReturn(mockMessage);
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockMessage.text()).thenReturn(start.command());
        when(mockChat.id()).thenReturn(chatId);

        SendMessage response = start.handle(update);

        var newUser = userRepository.findUserById(chatId);
        assertThat(newUser).isPresent();
        Assertions.assertAll(
            () -> assertThat(newUser.get().getId()).isEqualTo(chatId),
            () -> Assertions.assertEquals(RESPONSE_USER_SUCCESSFULLY_REGISTERED, response.getParameters().get("text"))
        );
    }

    @Test
    @DisplayName("Регистрация пользователя (корректные данные)")
    void registerUser_RepeatChatId() {
        var chatId = 2L;
        assertThat(userRepository.findUserById(chatId)).isEmpty();

        Message mockMessage = mock(Message.class);
        Chat mockChat = mock(Chat.class);
        when(update.message()).thenReturn(mockMessage);
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockMessage.text()).thenReturn(start.command());
        when(mockChat.id()).thenReturn(chatId);

        SendMessage response1 = start.handle(update);
        SendMessage response2 = start.handle(update);

        var newUser = userRepository.findUserById(chatId);
        assertThat(newUser).isPresent();
        Assertions.assertAll(
            () -> assertThat(newUser.get().getId()).isEqualTo(chatId),
            () -> Assertions.assertEquals(response1.getParameters().get("text"), RESPONSE_USER_SUCCESSFULLY_REGISTERED),
            () -> Assertions.assertEquals(response2.getParameters().get("text"), RESPONSE_USER_IS_ALREADY_REGISTERED)
        );
    }

}
