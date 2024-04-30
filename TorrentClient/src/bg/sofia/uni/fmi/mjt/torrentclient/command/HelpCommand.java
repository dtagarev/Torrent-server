package bg.sofia.uni.fmi.mjt.torrentclient.command;

import bg.sofia.uni.fmi.mjt.shared.command.Command;

import java.util.List;

public class HelpCommand extends BaseCommand implements Command {
    private static final int COMMAND_ARGUMENTS_COUNT = 0;
    private static final String correctFormat = "help";

    @Override
    public String execute(List<String> list) {
        checkNumberOfArguments(list, COMMAND_ARGUMENTS_COUNT, correctFormat);

        return "help";
    }
}
