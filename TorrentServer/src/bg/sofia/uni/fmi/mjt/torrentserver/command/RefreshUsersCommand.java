package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.User;

import java.net.Socket;
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
            Socket userSocket = data.get(user).socketChannel().socket();
            String inetAddress = userSocket.getInetAddress().toString();

            sb.append(user);
            sb.append("-").append(inetAddress);
            sb.append(":").append(userSocket.getPort());
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "refresh-users";
    }
}
