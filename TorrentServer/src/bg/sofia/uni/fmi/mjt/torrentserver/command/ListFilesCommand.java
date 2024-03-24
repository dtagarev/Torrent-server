package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListFilesCommand implements Command {
    ServerStorage storage;

    public ListFilesCommand(ServerStorage storage) {
        this.storage = storage;
    }


    @Override
    public String execute(List<String> list) {
        if(!list.isEmpty()) {
            return "Invalid command. No arguments needed.";
        }

        Map<String, Set<String>> data = storage.getData();
        String result = new String();

        for (String user : data.keySet()) {
            result += user + " : " + data.get(user).toString() + "\n";
        }
        result = result.substring(0, result.length() - 1);

        return result;
    }

    @Override
    public String toString() {
        return "list-files";
    }
}
