package bg.sofia.uni.fmi.mjt.torrentclient.command.ui;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.torrentclient.command.BaseCommand;

import java.util.List;

public class HelpCommand extends BaseCommand implements Command {
    private static final int COMMAND_ARGUMENTS_COUNT = 0;
    private static final String correctFormat = "help";

    @Override
    public String execute(List<String> list) {
        checkNumberOfArguments(list, COMMAND_ARGUMENTS_COUNT, correctFormat);

        StringBuilder sb = new StringBuilder();
        sb.append("Available commands:\n");
        sb.append("register <username> <file1,file2,fileN> - registers the user with the given username and files\n");
        sb.append("unregister <username> - unregisters the user with the given username\n");
        sb.append("list-files - lists all files in the server\n");
        sb.append("download <username> <file> <path-to-save> - downloads the file from the server\n");
        sb.append("help - prints this message\n");

        return sb.toString();
    }

    @Override
    public String toString() {
        return "help";
    }
}
