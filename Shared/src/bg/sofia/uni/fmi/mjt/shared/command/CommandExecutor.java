package bg.sofia.uni.fmi.mjt.shared.command;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.shared.exceptions.EmptyCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidSymbolInCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandExecutor {

    private Set<Command> commands;

    CommandExecutor(Set<Command> commands) {
        this.commands = commands;
    }

    private boolean commandContainsCorrectSymbols(String cmd) {
        //boolean b = cmd.matches("[a-zA-Z0-9 ,-]+");
        //System.out.println("correct symbol check is: " + b + " for command: " + cmd);
        //return b;
        return cmd.matches("[a-zA-Z0-9 ,-]+");
    }

    public void execute(String cmd) {
        List<String> cmdList = new ArrayList<>(List.of(cmd.split(" ")));

        if(cmd.isEmpty() || cmd.isBlank()) {
            throw new EmptyCommand("Command is empty");
        }
        if(!commandContainsCorrectSymbols(cmd)){
            throw new InvalidSymbolInCommand("Invalid symbol in command");
        }

        for (Command command : commands) {
            if(command.toString().equals(cmdList.getFirst())) {
                cmdList.removeFirst();
                command.execute(cmdList);
                return;
            }
        }

        throw new InvalidCommand("Invalid command: " + cmdList.getFirst());
    }
}
