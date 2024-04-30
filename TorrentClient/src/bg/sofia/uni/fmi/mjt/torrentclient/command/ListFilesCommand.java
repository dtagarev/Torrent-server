package bg.sofia.uni.fmi.mjt.torrentclient.command;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;

import java.util.List;

public class ListFilesCommand extends BaseCommand implements Command {
    private static final int COMMAND_ARGUMENTS_COUNT = 0;
    private static final String correctFormat = "list-files";

    @Override
    public String execute(List<String> list) throws InvalidCommand {
        checkCharacters(list);
        checkNumberOfArguments(list, COMMAND_ARGUMENTS_COUNT, correctFormat);

        return "list-files";
    }

    @Override
    public String toString() {
        return "list-files";
    }
}
