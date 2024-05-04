package bg.sofia.uni.fmi.mjt.torrentclient.refresher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerCommunicator;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.UserInterface;

public class UserRefresher implements Runnable {

    private ServerCommunicator serverCommunicator;
    private UsersFileManager usersFileManager;
    private ErrorHandler errorHandler;
    private UserInterface ui;

    public UserRefresher(ServerCommunicator serverCommunicator,
                         UsersFileManager usersFileManager,
                         ErrorHandler errorHandler,
                         UserInterface ui) {
        this.serverCommunicator = serverCommunicator;
        this.usersFileManager = usersFileManager;
        this.errorHandler = errorHandler;
        this.ui = ui;
    }

    @Override
    public void run() {
        try {
            while (true) {

                String message = "refresh-users";

                Thread.sleep(30_000);

                String reply = serverCommunicator.communicateWithServer(message);

                usersFileManager.writeToFile(reply);
            }

        } catch (IOException e) {
            errorHandler.writeToLogFile(e);
            ui.displayErrorMessage("There is a problem with the network communication.\n" +
                    "The active users will not be updated.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
