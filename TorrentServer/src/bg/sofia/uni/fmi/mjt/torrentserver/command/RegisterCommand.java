package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;

import bg.sofia.uni.fmi.mjt.shared.command.Command;

import java.util.List;

public class RegisterCommand implements Command {

    private final ServerStorage storage;
    private static final int NUMBER_OF_ARGUMENTS = 2;
    public RegisterCommand(ServerStorage storage) {
        this.storage = storage;
    }


    @Override
    public String execute(List<String> list) {
        if(list.size() < NUMBER_OF_ARGUMENTS) {
            return "Invalid command. Not enough arguments.\n" +
                "Please use the following format: register <username> <file1,file2,fileN>";
        } else if (list.size() > NUMBER_OF_ARGUMENTS) {
            return "Invalid command format. Too many arguments.\n" +
                "Please use the following format: register <username> <file1,file2,fileN>";
        }

        String username = list.getFirst();

        //the command assumes that the files are in the correct formant when passed
        List<String> args = List.of(list.get(1).split(","));

        if(storage.getData().containsKey(username)) {
            storage.register(username, args);
            return "Server storage updated with new user and files.";
        } else {
            return "There is no user with this username.";
        }
    }

    @Override
    public String toString() {
        return "register";
    }
}
