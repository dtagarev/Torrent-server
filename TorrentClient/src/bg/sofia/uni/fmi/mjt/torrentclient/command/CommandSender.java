package bg.sofia.uni.fmi.mjt.torrentclient.command;

import bg.sofia.uni.fmi.mjt.shared.exceptions.EmptyCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidSymbolInCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class CommandSender {

    private UserDirectory userDirectory;

    public CommandSender(UserDirectory userDirectory) {
        this.userDirectory = userDirectory;
    }

    private boolean isFileValid(String filename) {
        userDirectory.containsFile(filename);
        return true;
    }

    private boolean commandContainsCorrectSymbols(String cmd) {
        //boolean b = cmd.matches("[a-zA-Z0-9 ,-]+");
        //System.out.println("correct symbol check is: " + b + " for command: " + cmd);
        //return b;
        return cmd.matches("[a-zA-Z0-9 ,-]+");
    }


    public String send(String cmd)
            throws EmptyCommand, InvalidCommand, InvalidSymbolInCommand, FileNotFoundException {

        List<String> cmdParameters = new ArrayList<>(List.of(cmd.split(" ")));

        if(cmd.isEmpty() || cmd.isBlank()) {
            throw new EmptyCommand("Command is empty");
        }

        if(!commandContainsCorrectSymbols(cmd)){
            throw new InvalidSymbolInCommand("Invalid symbol in command");
        }

        if(!ServerCommandList.contains(cmdParameters.getFirst())) {
            throw new InvalidCommand("Invalid command: " + cmdParameters.getFirst());
        }

        if(cmdParameters.size() == 3) {
            List<String> files = new ArrayList<>(List.of(cmdParameters.getLast().split(",")));
            for(String file : files) {
                if(!userDirectory.containsFile(file)) {
                    throw new FileNotFoundException("File not found: " + file);
                }
            }
        }

        return "Command sent";
    }
}
