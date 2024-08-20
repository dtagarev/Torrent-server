package bg.sofia.uni.fmi.mjt.torrentclient.command;

import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerCommunicator;

public abstract class ServerCommunicationCommand extends  BaseCommand {

    protected final ServerCommunicator serverCommunicator;

    public ServerCommunicationCommand(ServerCommunicator serverCommunicator) {
        this.serverCommunicator = serverCommunicator;
    }

}
