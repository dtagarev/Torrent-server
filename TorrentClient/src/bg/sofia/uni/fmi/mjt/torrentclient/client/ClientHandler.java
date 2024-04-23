package bg.sofia.uni.fmi.mjt.torrentclient.client;

import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerConnection;
import bg.sofia.uni.fmi.mjt.torrentclient.exception.ServerConnectionException;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.ClientStorage;
import bg.sofia.uni.fmi.mjt.torrentclient.refresher.UsersFileManager;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.UserInterface;
import bg.sofia.uni.fmi.mjt.torrentclient.command.CommandChecker;

import java.io.File;
import java.nio.file.Path;

public class ClientHandler {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;

    ServerConnection serverConnection;
    ErrorHandler errorHandler;
    ClientStorage storage;
    CommandChecker commandChecker;
    UserInterface ui;
    UsersFileManager usersFileManager;

    public ClientHandler() {
        try {
            this.serverConnection = new ServerConnection(SERVER_HOST, SERVER_PORT, BUFFER_SIZE);
        } catch (ServerConnectionException e) {
            throw new RuntimeException(e);
        }

        Path logFilePath = Path.of(System.getProperty("user.dir") + File.separator + "clientLogs.txt");
        this.errorHandler = new ErrorHandler(logFilePath);
    }
}
