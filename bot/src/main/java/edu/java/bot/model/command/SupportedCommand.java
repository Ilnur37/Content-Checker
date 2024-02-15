package edu.java.bot.model.command;

import edu.java.bot.service.handler.CommandHandler;
import edu.java.bot.service.handler.HelpCommand;
import edu.java.bot.service.handler.ListCommand;
import edu.java.bot.service.handler.StartCommand;
import edu.java.bot.service.handler.TrackCommand;
import edu.java.bot.service.handler.UnTrackCommand;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SupportedCommand {
    private final CommandHandler commandHandler;

    public SupportedCommand(
        HelpCommand helpCommand,
        StartCommand startCommand,
        TrackCommand trackCommand,
        UnTrackCommand unTrackCommand,
        ListCommand listCommand
    ) {
        unTrackCommand.setNext(listCommand);
        trackCommand.setNext(unTrackCommand);
        startCommand.setNext(trackCommand);
        helpCommand.setNext(startCommand);
        this.commandHandler = helpCommand;
    }
}
