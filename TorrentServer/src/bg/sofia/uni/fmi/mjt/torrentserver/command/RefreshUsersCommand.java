package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RefreshUsersCommand implements Command {
    ServerStorage storage;

    public RefreshUsersCommand(ServerStorage storage) {
        this.storage = storage;
    }


    @Override
    public String execute(List<String> list) {
        if(!list.isEmpty()) {
            return "Invalid command. No arguments needed.";
        }

        Map<String , Set<String>> data = storage.getData();
        StringBuilder sb = new StringBuilder();
        for(String user : data.keySet()) {
            sb.append(user);
            for(String file : data.get(user)) {
                sb.append(" ").append(file);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public int getNumberOfArguments() {
        return 0;
    }

    @Override
    public int getFileArgumentIndex() {
        return 0;
    }

    @Override
    public String toString() {
        return "refresh-users";
    }
}
