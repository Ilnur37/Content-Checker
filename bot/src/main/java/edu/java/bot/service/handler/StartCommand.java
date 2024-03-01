package edu.java.bot.service.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.User;
import edu.java.bot.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class StartCommand extends CommandHandler {
    private static final String USER_SUCCESSFULLY_REGISTERED = "Пользователь успешно зарестрирован";
    private static final String RESPONSE_USER_IS_ALREADY_REGISTERED = "Вы уже зарагестрированны";
    private static final String RESPONSE_USER_SUCCESSFULLY_REGISTERED = "Поздравляю, регистрация прошла успешно!";
    protected static final String USER_MUST_BE_REGISTERED =
        "Прежде чем пользоваться фцнкциями бота, вам необходимо зарегестрироваться. Введите команду \"/start\"";

    public StartCommand(UserRepository userRepository, TrackCommand trackCommand) {
        super(userRepository);
        this.next = trackCommand;
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Зарегестрировать пользователя";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();

        var isTheCorrectCommand = checkingThatThisIsTheCorrectCommand(this, update.message().text(), update);
        if (isTheCorrectCommand != null) {
            return isTheCorrectCommand;
        }

        StringBuilder response = new StringBuilder();
        if (userRepository.findUserById(chatId).isPresent()) {
            response.append(RESPONSE_USER_IS_ALREADY_REGISTERED);
        } else {
            userRepository.saveUser(new User(chatId));
            LOGGER.info(String.format(CHAT_ID_FOR_LOGGER, chatId) + USER_SUCCESSFULLY_REGISTERED);
            response.append(RESPONSE_USER_SUCCESSFULLY_REGISTERED);
        }
        return new SendMessage(chatId, response.toString());
    }

    @Override
    public SendMessage checkingThatThisIsTheCorrectCommand(
        CommandHandler currCommandHandler,
        String updateCommand,
        Update update
    ) {
        Long chatId = update.message().chat().id();
        if (!update.message().text().equals(command())) {
            if (userRepository.findUserById(chatId).isEmpty()) {
                return new SendMessage(chatId, USER_MUST_BE_REGISTERED);
            } else if (next != null) {
                return next.handle(update);
            } else {
                return new SendMessage(chatId, UNSUPPORTED_COMMAND);
            }
        }
        return null;
    }

}
