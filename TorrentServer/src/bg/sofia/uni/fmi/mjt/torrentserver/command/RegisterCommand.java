package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.shared.Command;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;

import java.util.List;

public class RegisterCommand implements Command {

    private ServerStorage storage;

    public RegisterCommand(ServerStorage storage) {
        this.storage = storage;
    }

    private List<String> formatFileString(String s) {
        String[] tokens = s.split(",");
        List<String> files = List.of(tokens);
        return files;
    }

    @Override
    public void execute(String s) {
        String[] tokens = s.split(" ");
        String username = tokens[1];
        List<String> files = formatFileString(tokens[2]);
        storage.register(username, files);
    }
}
