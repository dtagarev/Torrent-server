package bg.sofia.uni.fmi.mjt.torrentclient.command;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;

import java.util.List;

public class RegisterCommand extends BaseCommand implements Command {
    private static final int COMMAND_ARGUMENTS_COUNT = 2;
    private static final String correctFormat = "register <username> <file1,file2,fileN>";


    @Override
    public String execute(List<String> list) throws InvalidCommand {
        checkCharacters(list);
        checkNumberOfArguments(list, COMMAND_ARGUMENTS_COUNT, correctFormat);
        checkUsername(list.get(0));
        checkIfFilesAreReal(list.get(1));

        return "register " + list.get(0) + " " + list.get(1);
    }


    @Override
    public String toString() {
        return "register";
    }
}
