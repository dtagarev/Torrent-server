package bg.sofia.uni.fmi.mjt.torrentclient.refresher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.UserInterface;

public class UserRefresher implements Runnable {

    private final int SERVER_PORT;
    private final String SERVER_HOST;
    private static final int BUFFER_SIZE = 4096;
    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    private String username;
    private UsersFileManager usersFileManager;
    private ErrorHandler errorHandler;
    private UserInterface ui;

    public UserRefresher(int SERVER_PORT, String SERVER_HOST,
                         String username, ErrorHandler errorHandler, UserInterface ui, UsersFileManager usersFileManager) {
        this.SERVER_PORT = SERVER_PORT;
        this.SERVER_HOST = SERVER_HOST;
        this.username = username;
        this.errorHandler = errorHandler;
        this.ui = ui;
        this.usersFileManager = usersFileManager;
    }

    @Override
    public void run() {

        try (SocketChannel socketChannel = SocketChannel.open()) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            //TODO: daemon thread that sends a message to the server every 30 seconds

            boolean firstRun = true;

            while (true) {

                String message = "refresh";

                if(firstRun) {
                    firstRun = false;
                    message = "refresher " + username;
                }
                Thread.sleep(30000);
                writeToServer(socketChannel, message);

                String reply = readFromServer(socketChannel);

                usersFileManager.writeToFile(reply);
            }

        } catch (IOException | InterruptedException e) {
            errorHandler.writeToLogFile(e);
            ui.displayErrorMessage("There is a problem with the network communication.\n" +
                "The active users will not be updated.");
        }
    }

    private String readFromServer(SocketChannel socketChannel) throws UnsupportedEncodingException {
        buffer.clear(); // switch to writing mode
        try {
            socketChannel.read(buffer); // buffer fill
        } catch (IOException e) {
            errorHandler.writeToLogFile(e);
            ui.displayErrorMessage("Error getting active users info from server.");
        }

        buffer.flip(); // switch to reading mode
        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);

        return new String(byteArray, StandardCharsets.UTF_8); // buffer drain
    }


    void writeToServer(SocketChannel socketChannel, String message) {
        buffer.clear(); // switch to writing mode
        buffer.put(message.getBytes()); // buffer fill
        buffer.flip(); // switch to reading mode
        try {
            socketChannel.write(buffer); // buffer drain
        } catch (IOException e) {
            errorHandler.writeToLogFile(e);
            ui.displayErrorMessage("Error sending message to server.");
        }
    }

}
