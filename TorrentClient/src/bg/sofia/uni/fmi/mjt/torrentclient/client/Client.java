package bg.sofia.uni.fmi.mjt.torrentclient.client;

import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

// NIO, blocking
public class Client {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;
    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public static void main(String[] args) {
        ClientManager clientManager = new ClientManager();
        //TODO: there should be try catch for errors thrown by function
        clientManager.setupNewClient();

        try {
            communicateWithServer(clientManager);
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }

    private static void communicateWithServer(ClientManager clientManager) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        Scanner scanner = new Scanner(System.in);
        socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

        System.out.println("Connected to the server.");

        boolean firstRun = true;
        while (true) {
            String message = "";
            if(firstRun) {
                System.out.print("Enter name: ");
                message = clientManager.enterName(scanner.nextLine());
                firstRun = false;
            } else {
                System.out.print("Enter message: ");
                message = clientManager.enterCommand(scanner.nextLine());
            }

            if ("quit".equals(message)) {
                break;
            }

            System.out.println("Sending message <" + message + "> to the server...");
            writeToServer(socketChannel, message);

            String reply = readFromServer(socketChannel);
            //System.out.println("The server replied <" + reply + ">");
            System.out.println(reply);
        }

    }

    private static String readFromServer(SocketChannel socketChannel) throws IOException {
        buffer.clear(); // switch to writing mode
        socketChannel.read(buffer); // buffer fill
        buffer.flip(); // switch to reading mode

        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        return new String(byteArray, "UTF-8"); // buffer drain
    }

    private static void writeToServer(SocketChannel socketChannel, String message) throws IOException {
        buffer.clear(); // switch to writing mode
        buffer.put(message.getBytes()); // buffer fill
        buffer.flip(); // switch to reading mode
        socketChannel.write(buffer); // buffer drain
    }
}
