package bg.sofia.uni.fmi.mjt.shared;

import java.util.List;
import java.util.Set;

public class CommandExecutor {


    private Set<Command> commands;

    CommandExecutor(Set<Command> commands) {
        this.commands = commands;
    }

    private String[] formatString(String cmd) {
        return cmd.split(" ");
    }

    private boolean isValidCommand(List<String> cmd) {

        for (Command command : commands) {
            if(command.toString().equals(cmd.getFirst())){
                return true;
            }
        }
        return false;
    }
    private boolean commandContainsCorrectSymbols(String cmd) {
        return cmd.matches("[a-zA-Z0-9 ,-]+");
    }

    public void execute(String cmd) {
        List<String> cmdList = List.of(formatString(cmd));

        for (Command command : commands) {
            if(command.toString().equals(cmd)){
                command.execute(cmd);
                return;
            }
        }
        System.out.println("Invalid command");
    }
}
