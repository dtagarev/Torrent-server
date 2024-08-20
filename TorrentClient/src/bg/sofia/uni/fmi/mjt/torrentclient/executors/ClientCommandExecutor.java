package bg.sofia.uni.fmi.mjt.torrentclient.executors;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.shared.exceptions.EmptyCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ClientCommandExecutor {
    private final Set<Command> commands;

    public ClientCommandExecutor(Set<Command> commands) {
        this.commands = commands;
    }

    public String execute(String cmd) {
        List<String> cmdList = new ArrayList<>(List.of(cmd.split(" ")));

        if (cmd.isEmpty() || cmd.isBlank()) {
            throw new EmptyCommand("Command is empty");
        }

        final Optional<Command> command = commands.stream()
            .filter(c -> c.toString().equals(cmdList.getFirst()))
            .findFirst();

        if (command.isEmpty()) {
            throw new InvalidCommand("Invalid command: " + cmdList.getFirst());
        }

        return command.get().execute(cmdList.subList(1, cmdList.size()));
    }
}
