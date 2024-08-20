package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;
import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;

import java.util.Arrays;
import java.util.List;

public class UnregisterCommand implements Command {
    private ServerStorage storage;

    private ErrorHandler errorHandler;

    private static final int NUMBER_OF_ARGUMENTS = 2;

    public UnregisterCommand(ServerStorage storage, ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        this.storage = storage;
    }

    @Override
    public String execute(List<String> list) {
        if (list.size() < NUMBER_OF_ARGUMENTS) {
            return "Invalid command. Not enough arguments.\n" +
                "Please use the following format: unregister <username> <file1,file2,...,fileN>";
        } else if (list.size() > NUMBER_OF_ARGUMENTS) {
            return "Invalid command format. Too many arguments.\n" +
                "Please use the following format: unregister <username> <file1,file2,...,fileN>";
        }

        String username = list.getFirst();
        List<String> removedFiles =  Arrays.stream(list.get(1).split(",")).toList();

        try {
            storage.unregister(username, removedFiles);
            return "Server storage updated. The files: " + removedFiles + " have been removed from user: " + username;
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
