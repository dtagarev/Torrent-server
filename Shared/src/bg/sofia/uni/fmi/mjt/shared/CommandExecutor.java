package bg.sofia.uni.fmi.mjt.shared;

import java.util.Set;

public class CommandExecutor {

    Set<Command> commands;

    CommandExecutor(Set<Command> commands) {
        this.commands = commands;
    }

    public void execute(String cmd) {
        for (Command command : commands) {
            if(command.toString().equals(cmd)){
                command.execute(cmd);
                return;
            }
        }
        System.out.println("Invalid command");
    }
}
