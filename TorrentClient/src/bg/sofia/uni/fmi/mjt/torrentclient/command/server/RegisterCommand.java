package bg.sofia.uni.fmi.mjt.torrentclient.command.server;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.command.ServerCommunicationCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerCommunicator;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;

import java.io.IOException;
import java.util.List;

public class RegisterCommand extends ServerCommunicationCommand implements Command {
    private static final int COMMAND_ARGUMENTS_COUNT = 2;
    private static final String correctFormat = "register <username> <file1,file2,fileN>";

    private final UserDirectory storage;

    public RegisterCommand(ServerCommunicator serverCommunicator, UserDirectory storage) {
        super(serverCommunicator);
        this.storage = storage;
    }

    @Override
    public String execute(List<String> list) throws InvalidCommand {
        checkCharacters(list);
        checkNumberOfArguments(list, COMMAND_ARGUMENTS_COUNT, correctFormat);
        checkUsername(list.get(0));
        checkIfFilesAreReal(list.get(1));

        try {
            String response = serverCommunicator.communicateWithServer("register " + list.get(0) + " " + list.get(1));
            storage.addFilePath(list.get(1));
            return response;
        } catch (IOException e) {
            throw new InvalidCommand("Could not communicate with the server");
        }

    }


    @Override
    public String toString() {
        return "register";
    }
}
