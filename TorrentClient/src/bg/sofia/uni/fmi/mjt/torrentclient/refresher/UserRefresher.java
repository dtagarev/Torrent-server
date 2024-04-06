package bg.sofia.uni.fmi.mjt.torrentclient.refresher;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;

public class UserRefresher implements Runnable {

    private final int SERVER_PORT;
    private final String SERVER_HOST;
    private static final int BUFFER_SIZE = 4096;
    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    private String username;
    private Path filePath;
    private ErrorHandler errorHandler;

    public UserRefresher(int SERVER_PORT, String SERVER_HOST,
                         String username, ErrorHandler errorHandler) {
        this.SERVER_PORT = SERVER_PORT;
        this.SERVER_HOST = SERVER_HOST;
        this.username = username;
        this.errorHandler = errorHandler;
        filePath = Path.of(System.getProperty("user.dir")
            + System.lineSeparator()
            + "connectedUsers.txt");
    }

    @Override
    public void run() {
        createUserFile();

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

                writeToFile(reply);
            }

        } catch (IOException e) {
            errorHandler.writeToLogFile(e);
            System.out.println("There is a problem with the network communication.\n"
                + "The active users will not be updated.");
        } catch (InterruptedException e) {
            errorHandler.writeToLogFile(e);
            System.out.println("There is a problem with the network communication.\n"
                + "The active users will not be updated.");
        }
    }

    private String readFromServer(SocketChannel socketChannel) throws UnsupportedEncodingException {
        buffer.clear(); // switch to writing mode
        try {
            socketChannel.read(buffer); // buffer fill
        } catch (IOException e) {
            errorHandler.writeToLogFile(e);
            System.out.println("Error getting active users info from server.");
        }

        buffer.flip(); // switch to reading mode
        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);

        return new String(byteArray, "UTF-8"); // buffer drain
    }

    private void createUserFile() {
        if(!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                errorHandler.writeToLogFile(e);
                System.out.println("Error while creating active users file.");
            }
        }

    }

    void writeToServer(SocketChannel socketChannel, String message) {
        buffer.clear(); // switch to writing mode
        buffer.put(message.getBytes()); // buffer fill
        buffer.flip(); // switch to reading mode
        try {
            socketChannel.write(buffer); // buffer drain
        } catch (IOException e) {
            errorHandler.writeToLogFile(e);
            System.out.println("Error getting user info from server.");
        }
    }

    void writeToFile(String message) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath.toString(), false);
            fos.write(message.getBytes());
            fos.close();
        } catch (IOException e) {
            errorHandler.writeToLogFile(e);
            System.out.println("Error while updating active users.");
        }
    }
}
