package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;
import bg.sofia.uni.fmi.mjt.shared.command.Command;

import java.util.List;

public class HelpCommand implements Command {
    private ServerStorage storage;

    public HelpCommand(ServerStorage storage) {
        this.storage = storage;
    }


    @Override
    public String execute(List<String> list) {
        if(!list.isEmpty()) {
            return "Invalid command. No arguments needed.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Available commands:\n");
        sb.append("register <user> <file1,file2,fileN>\n");
        sb.append("unregister <user> <file1,file2,fileN>\n");
        sb.append("list-files\n");
        sb.append("help\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "help";
    }
}
