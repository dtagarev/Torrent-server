package bg.sofia.uni.fmi.mjt.torrentclient.executors;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.shared.exceptions.EmptyCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.command.RegisterCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GlobalCommandExecutor {
    private Set<Command> commands;

    public GlobalCommandExecutor(Set<Command> commands) {
        this.commands = commands;
    }

    void execute(String cmd) {
        List<String> cmdList = new ArrayList<>(List.of(cmd.split(" ")));

        if(cmd.isEmpty() || cmd.isBlank()) {
            throw new EmptyCommand("Command is empty");
        }
        final var command = commands.stream().findFirst().filter(c -> c.toString().equals(cmdList.getFirst()));
        if (command.isEmpty()) {
            throw new InvalidCommand("Invalid command: " + cmdList.getFirst());
        }

        command.get().execute(cmdList);
    }
}
