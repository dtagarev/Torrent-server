package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListFilesCommand implements Command {
    private ServerStorage storage;

    public ListFilesCommand(ServerStorage storage) {
        this.storage = storage;
    }


    @Override
    public String execute(List<String> list) {
        if(!list.isEmpty()) {
            return "Invalid command. No arguments needed.";
        }

        Map<String, User> data = storage.getData();
        StringBuilder result = new StringBuilder();

        for (String user : data.keySet()) {
            User userData = data.get(user);
            if(userData.ClientServerPort() != 0) {
                result.append(user).append(" : ").append(userData.files().toString()).append("\n");
            }
        }
        result = new StringBuilder(result.substring(0, result.length() - 1));

        return result.toString();
    }

    @Override
    public String toString() {
        return "list-files";
    }
}
