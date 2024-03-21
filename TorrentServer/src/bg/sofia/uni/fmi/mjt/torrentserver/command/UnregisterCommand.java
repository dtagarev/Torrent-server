package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;

import java.util.List;

public class UnregisterCommand implements Command {
    ServerStorage storage;
    public UnregisterCommand(ServerStorage storage) {
        this.storage = storage;
    }
    @Override
    public void execute(List<String> list) {
        String username = list.getFirst();
        try {
            storage.unregister(username);
        } catch (IllegalArgumentException e) {
            //TODO: log the exception
            //TODO: what to do here?
            System.out.println(e.getMessage());
        }
    }
}
