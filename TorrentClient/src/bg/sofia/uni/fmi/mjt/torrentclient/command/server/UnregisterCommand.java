package bg.sofia.uni.fmi.mjt.torrentclient.command.server;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.command.ServerCommunicationCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerCommunicator;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;

import java.io.IOException;
import java.util.List;

public class UnregisterCommand extends ServerCommunicationCommand implements Command {
    private static final int COMMAND_ARGUMENTS_COUNT = 2;
    private static final String correctFormat = "unregister <username> <file1,file2,fileN>";

    private UserDirectory userDirectory;

    public UnregisterCommand(ServerCommunicator serverCommunicator, UserDirectory userDirectory) {
        super(serverCommunicator);
        this.userDirectory = userDirectory;
    }

    @Override
    public String execute(List<String> list) throws InvalidCommand {
        checkCharacters(list);
        checkNumberOfArguments(list, COMMAND_ARGUMENTS_COUNT, correctFormat);
        checkUsername(list.get(0));
        checkIfFilesAreReal(list.get(1));

        try {
            String reply = serverCommunicator.communicateWithServer("unregister " + list.get(0) + " " + list.get(1));
            userDirectory.removeFilePath(list.get(1));
            return reply;
        } catch (IOException e) {
            throw new InvalidCommand("Could not communicate with the server");
        }
    }

    @Override
    public String toString() {
        return "unregister";
    }
}
