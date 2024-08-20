package bg.sofia.uni.fmi.mjt.torrentclient.command;

import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;

import java.util.List;

public abstract class BaseCommand  {

    protected void checkCharacters(List<String> list) throws InvalidCommand {
        for (String s : list) {
            if (!SyntaxChecker.checkCommandContainsCorrectSymbols(s)) {
                throw new InvalidCommand("Invalid characters in command");
            }
        }
    }

    protected void checkNumberOfArguments(List<String> list, int numberOfArguments, String correctFormat)
        throws InvalidCommand {
        if (list.size() < numberOfArguments) {

            throw new InvalidCommand("Invalid number of arguments. Not enough arguments\n" +
                "Please use the following format: " + correctFormat);
        } else if (list.size() > numberOfArguments) {

            throw new InvalidCommand("Invalid number of arguments. Too many arguments\n" +
                "Please use the following format: " + correctFormat);
        }
    }

    protected void checkIfFilesAreReal(String files) throws InvalidCommand {
        String[] list = files.split(",");

        for (String file : list) {
            if (!UserDirectory.isAFile(file)) {
                throw new InvalidCommand("File " + file + " does not exist");
            }
        }
    }

    protected void checkUsername(String username) throws InvalidCommand {
        if (!SyntaxChecker.checkUsername(username)) {
            throw new InvalidCommand("Invalid username.\n Username should contain only lowercase letters and digits.");
        }
    }
}
