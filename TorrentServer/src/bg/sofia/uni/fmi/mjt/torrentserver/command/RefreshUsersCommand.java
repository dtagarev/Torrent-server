package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.User;

import java.util.List;
import java.util.Map;

public class RefreshUsersCommand implements Command {
    private ServerStorage storage;


    public RefreshUsersCommand(ServerStorage storage) {
        this.storage = storage;
    }


    @Override
    public String execute(List<String> list) {
        if(!list.isEmpty()) {
            return "Invalid command, No arguments needed.\n"
                + "Please use the following format: refresh-users";
        }

        Map<String , User> data = storage.getData();
        StringBuilder sb = new StringBuilder();
        for(String user : data.keySet()) {
            sb.append(user);
            String inetAddress = data.get(user).socketChannel().socket().getInetAddress().toString();
            sb.append("-").append(inetAddress);
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "refresh-users";
    }
}
