package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;
import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;

import java.util.List;

public class UnregisterCommand implements Command {
    private ServerStorage storage;

    private ErrorHandler errorHandler;

    public UnregisterCommand(ServerStorage storage, ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        this.storage = storage;
    }

    @Override
    public String execute(List<String> list) {
        if (list.size() < 1) {
            return "Invalid command. Not enough arguments.";
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
    public String toString() {
        return "unregister";
    }
}
