package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;

import bg.sofia.uni.fmi.mjt.shared.command.Command;

import java.util.List;

public class RegisterCommand implements Command {

    private ServerStorage storage;

    public RegisterCommand(ServerStorage storage) {
        this.storage = storage;
    }


    @Override
    public void execute(List<String> list) {
        String username = list.getFirst();
        storage.register(username, list.subList(1, list.size()));

    }
}
