package bg.sofia.uni.fmi.mjt.shared;

import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidSymbolInCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandExecutor {

    // TODO: more checks for the command
    // like if the command is empty
    // think of other ways to check the command
    // when ready comment out the print statements
    private Set<Command> commands;

    CommandExecutor(Set<Command> commands) {
        this.commands = commands;
    }

    private String[] formatString(String cmd) {
        return cmd.split(" ");
    }

    //Todo: this function may be useless
    private boolean isValidCommand(List<String> cmd) {

        for (Command command : commands) {
            System.out.println("Command: " + command.toString());

            if(command.toString().equals(cmd.getFirst())){
                System.out.println("isValidCommand returning true");
                return true;
            }
        }
        System.out.println("isValidCommand returning false");
        return false;
    }
    private boolean commandContainsCorrectSymbols(String cmd) {
        boolean b = cmd.matches("[a-zA-Z0-9 ,-]+");
        System.out.println("correct symbol check is: " + b + " for command: " + cmd + ")");
        return b;
    }

    public void execute(String cmd) {
        //format string may need something else?
        List<String> cmdList = new ArrayList<>(List.of(formatString(cmd)));

        if(cmdList.isEmpty()) {
            throw new InvalidCommand("Command is empty");
        }
        if(!commandContainsCorrectSymbols(cmd)){
            throw new InvalidSymbolInCommand("Invalid symbol in command");
        }
        if(!isValidCommand(cmdList)) {
            throw new InvalidCommand("Invalid command: " + cmdList.getFirst());
        }

        for (Command command : commands) {
            if(command.toString().equals(cmdList.getFirst())) {
                cmdList.removeFirst();
                command.execute(cmdList);
                return;
            }
        }
    }
}
