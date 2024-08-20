package bg.sofia.uni.fmi.mjt.torrentserver.command.executors;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.shared.exceptions.EmptyCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandExecutor {

    private final Set<Command> commands;

    public CommandExecutor(Set<Command> commands) {
        this.commands = commands;
    }

    public String execute(String cmd) throws EmptyCommand, InvalidCommand {
        List<String> cmdList = new ArrayList<>(List.of(cmd.split(" ")));

        if (cmd.isEmpty() || cmd.isBlank()) {
            throw new EmptyCommand("Command is empty");
        }

        for (Command command : commands) {
            if (command.toString().equals(cmdList.getFirst())) {
                cmdList.removeFirst();
                return command.execute(cmdList);
            }
        }

        throw new InvalidCommand("Invalid command: " + cmdList.getFirst());
    }

}