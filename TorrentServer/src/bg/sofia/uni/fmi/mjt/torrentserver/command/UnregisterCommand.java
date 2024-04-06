package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;
import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;

import java.util.List;

public class UnregisterCommand implements Command {
    private ServerStorage storage;

    private ErrorHandler errorHandler;

    private static final int NUMBER_OF_ARGUMENTS = 1;

    public UnregisterCommand(ServerStorage storage, ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        this.storage = storage;
    }

    @Override
    public String execute(List<String> list) {
        if (list.isEmpty()) {
            return "Invalid command. Not enough arguments.";
        } else if(list.size() > NUMBER_OF_ARGUMENTS) {
            return "Invalid command format. Too many arguments.\n" +
                "Please use the following format: unregister <username>";
        }
        String username = list.getFirst();
        try {
            storage.unregister(username);
            return "Server storage updated. User " + username + " unregistered.";
        } catch (IllegalArgumentException e) {
            errorHandler.writeToLogFile(e);
            return e.getMessage();
        }
    }

    @Override
    public int getNumberOfArguments() {
        return 3;
    }

    @Override
    public int getFileArgumentIndex() {
        return 2;
    }

    @Override
    public String toString() {
        return "unregister";
    }
}
