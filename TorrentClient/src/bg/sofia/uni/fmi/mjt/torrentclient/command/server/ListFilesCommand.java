package bg.sofia.uni.fmi.mjt.torrentclient.command.server;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.command.ServerCommunicationCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerCommunicator;

import java.io.IOException;
import java.util.List;

public class ListFilesCommand extends ServerCommunicationCommand implements Command {
    private static final int COMMAND_ARGUMENTS_COUNT = 0;
    private static final String correctFormat = "list-files";

    public ListFilesCommand(ServerCommunicator serverCommunicator) {
        super(serverCommunicator);
    }


    @Override
    public String execute(List<String> list) {
        checkCharacters(list);
        checkNumberOfArguments(list, COMMAND_ARGUMENTS_COUNT, correctFormat);

        try {
            return serverCommunicator.communicateWithServer("list-files");
        } catch (IOException e) {
            throw new InvalidCommand("Could not communicate with the server");
        }
    }

    @Override
    public String toString() {
        return "list-files";
    }
}
