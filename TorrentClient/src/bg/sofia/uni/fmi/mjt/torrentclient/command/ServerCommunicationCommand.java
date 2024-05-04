package bg.sofia.uni.fmi.mjt.torrentclient.command;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerCommunicator;

import java.io.IOException;

public abstract class ServerCommunicationCommand extends  BaseCommand {

    final protected ServerCommunicator serverCommunicator;

    public ServerCommunicationCommand(ServerCommunicator serverCommunicator) {
        this.serverCommunicator = serverCommunicator;
    }


}
