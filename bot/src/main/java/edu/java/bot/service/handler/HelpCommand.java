package edu.java.bot.service.handler;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelpCommand extends CommandHandler {
    private static final String RESPONSE_LIST_OF_SUPPORTED_COMMANDS =
        "Вы можете воспользоваться следующими командами:\n";

    @Autowired
    public HelpCommand(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Вывести окно с командами";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        StringBuilder response = new StringBuilder();
        if (!update.message().text().equals(command())) {
            if (next != null) {
                return next.handle(update);
            }
            return new SendMessage(chatId, UNSUPPORTED_COMMAND);
        }

        response.append(RESPONSE_LIST_OF_SUPPORTED_COMMANDS);
        response.append(getStringOfCommands());
        return new SendMessage(chatId, response.toString());

    }

    private StringBuilder getStringOfCommands() {
        StringBuilder resultStrOfCommands = new StringBuilder();
        List<BotCommand> commands = getListOfCommand();
        commands.forEach(command ->
            resultStrOfCommands.append(String.format("%s : %s\n", command.command(), command.description())
            )
        );
        return resultStrOfCommands;
    }

    private List<BotCommand> getListOfCommand() {
        List<BotCommand> commands = new ArrayList<>();
        CommandHandler tempCom = this;
        do {
            commands.add(tempCom.toApiCommand());
            tempCom = tempCom.getNext();
        } while (tempCom != null);
        return commands;
    }
}
