package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;

import java.util.List;
import java.util.Set;

public class RegisterCommand implements Command {

    private ServerStorage storage;

    public RegisterCommand(ServerStorage storage) {
        this.storage = storage;
    }


    @Override
    public void execute(List<String> list) {
        String username = list.getFirst();
        list.removeFirst();
        storage.register(username, Set.copyOf(list));
    }
}
