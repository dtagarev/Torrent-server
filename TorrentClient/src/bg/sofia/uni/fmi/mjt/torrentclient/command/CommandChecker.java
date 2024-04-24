package bg.sofia.uni.fmi.mjt.torrentclient.command;

import bg.sofia.uni.fmi.mjt.shared.exceptions.EmptyCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidSymbolInCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class CommandChecker {

    private final UserDirectory userDirectory;

    public CommandChecker(UserDirectory userDirectory) {
        this.userDirectory = userDirectory;
    }

    private boolean isFileValid(String filename) {
        return userDirectory.containsFilePath(filename);
    }

    private boolean commandContainsCorrectSymbols(String cmd) {
        return cmd.matches("[a-zA-Z0-9. \\\\/,-]+");
    }

    private String areFilesValid(String files) {
        String[] fileList = files.split(",");

        for(String file : fileList) {
            if(!isFileValid(file)) {
                return file;
            }
        }
        return null;
    }

    public boolean checkUsername(String name) {
        if(name.matches("[a-z0-9]+")) {
            return true;
        }
        return false;
    }

    public String check(String cmd)
            throws EmptyCommand, InvalidCommand, InvalidSymbolInCommand, FileNotFoundException {

        List<String> cmdParameters = new ArrayList<>(List.of(cmd.split(" ")));

        if (cmd.isEmpty() || cmd.isBlank()) {
            throw new EmptyCommand("Command is empty");
        }
        if (!commandContainsCorrectSymbols(cmd)) {
            throw new InvalidSymbolInCommand("Invalid symbol in command");
        }

        AvailableCommands serverCommand;
        try {
            String commandName = cmdParameters.getFirst();
            if(commandName.contains("-")) {
                commandName = commandName.replace("-", "_");
            }
            serverCommand = AvailableCommands.valueOf(commandName);
        } catch (IllegalArgumentException e) {
            throw new InvalidCommand("Invalid command");
        }

        if (cmdParameters.size() != serverCommand.getCommandArgs()) {
            throw new InvalidCommand("Invalid number of arguments");
        }

        if (serverCommand.getFileArgsIdx() != -1){
            String invalidFile = areFilesValid(cmdParameters.get(serverCommand.getFileArgsIdx()));
            if(invalidFile != null) {
                throw new FileNotFoundException("File not found: " + invalidFile);
            }
        }

        return cmd;
    }
}
