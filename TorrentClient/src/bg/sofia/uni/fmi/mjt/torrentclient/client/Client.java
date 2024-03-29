package bg.sofia.uni.fmi.mjt.torrentclient.client;

import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.Cli;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.UserInterface;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;
    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public static void main(String[] args) {
        UserInterface ui = new Cli();
        ClientManager clientManager = new ClientManager(ui);

        try {
            communicateWithServer(clientManager, ui);
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }

    private static void communicateWithServer(ClientManager clientManager, UserInterface ui) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        Scanner scanner = new Scanner(System.in);
        socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

        ui.displayConnectedToServer();

        boolean firstRun = true;
        while (true) {
            String message;
            if(firstRun) {
                do {
                    ui.displayNamePrompt();
                    message = scanner.nextLine();
                } while (!clientManager.checkName(message));
                firstRun = false;
            } else {
                do {
                    ui.displayMessagePrompt();
                    message = scanner.nextLine();
                } while (!clientManager.checkCommand(message));
            }

            if ("quit".equals(message)) {
                break;
            }

            ui.displaySendingMessage(message);
            writeToServer(socketChannel, message);

            String reply = readFromServer(socketChannel);
            ui.displayServerReply(reply);
        }

    }

    private static String readFromServer(SocketChannel socketChannel) throws IOException {
        buffer.clear(); // switch to writing mode
        socketChannel.read(buffer); // buffer fill
        buffer.flip(); // switch to reading mode

        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        return new String(byteArray, StandardCharsets.UTF_8); // buffer drain
    }

    private static void writeToServer(SocketChannel socketChannel, String message) throws IOException {
        buffer.clear(); // switch to writing mode
        buffer.put(message.getBytes()); // buffer fill
        buffer.flip(); // switch to reading mode
        socketChannel.write(buffer); // buffer drain
    }
}
