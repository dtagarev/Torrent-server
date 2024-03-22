package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;

import bg.sofia.uni.fmi.mjt.shared.command.Command;

import java.util.List;

public class RegisterCommand implements Command {

    private final ServerStorage storage;
    private final String commandNotation = "register";

    public RegisterCommand(ServerStorage storage) {
        this.storage = storage;
    }


    @Override
    public void execute(List<String> list) {
        String username = list.getFirst();
        if(storage.getData().containsKey(username)) {
            storage.register(username, list.subList(1, list.size()));
            //return "Server storage updated with new user and files."
        } else {
            //return "There is no user with this username."
        }
    }

    @Override
    public String toString() {
        return commandNotation;
    }
}
