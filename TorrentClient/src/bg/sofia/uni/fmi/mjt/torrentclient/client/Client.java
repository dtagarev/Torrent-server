package bg.sofia.uni.fmi.mjt.torrentclient.client;

import bg.sofia.uni.fmi.mjt.torrentclient.miniserver.MiniServer;
import bg.sofia.uni.fmi.mjt.torrentclient.refresher.UserRefresher;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.Cli;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.UserInterface;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

public class Client {
    // TODO: client miniServer
    // TODO: separate MiniClient(can be integrated in the real client a function like Download file
    //  and it is just connecting to the socket and downloanding the file but can i make the storage of the files
    //  block only on files that it is downloading or is curretnly sending, because now the whole
    //  storage is blocked which is not ideal, it can be like 1 min download) and MiniServer that only job is to listen
    //  for connections and download files
    // Todo: How to multithread management in java
    // Todo: ShutDown trough the name port
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;
    private static final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public static void main(String[] args) throws InterruptedException {
        UserInterface ui = new Cli();
        ClientManager clientManager = new ClientManager(ui);
        ExecutorService executor = newVirtualThreadPerTaskExecutor();

        try {
            SocketChannel socketChannel = connectToServer(ui, clientManager);

            String clientHost = ((InetSocketAddress) socketChannel.getLocalAddress()).getHostString();
            int clientPort = ((InetSocketAddress) socketChannel.getLocalAddress()).getPort();

            String clientName = setClientName(clientManager, ui, socketChannel);

            UserRefresher refresher =
                new UserRefresher(SERVER_PORT, SERVER_HOST,
                    clientName,
                    clientManager.getErrorHandler(),
                    ui, clientManager.getUsersFileManager());

            Thread refresherThread = new Thread(refresher);
            refresherThread.setDaemon(true);
            executor.submit(refresherThread);

            Thread miniServerThread = new Thread(
                    new MiniServer(clientHost, clientPort, clientManager.getStorage(), clientManager.getErrorHandler()));
            executor.submit(miniServerThread);

            communicateWithServer(socketChannel, clientManager, ui);
            int test = 0;


        } catch (IOException e) {
            clientManager.getErrorHandler().writeToLogFile(e);
            throw new RuntimeException("There is a problem with the network communication", e);
        } finally {
            System.out.println("Client: Shutting down the client" + "Calling awaitTermination");
            executor.shutdownNow();
            executor.awaitTermination(5, TimeUnit.SECONDS);
            System.out.println("Client: Time's up, shutting down the client");

        }
    }

    private static void sendRefreshActiveUsersCommandToServer(SocketChannel socketChannel, String username,
                                                              ClientManager clientManager) throws IOException {
        String message = "refresh-users";
        writeToServer(socketChannel, message);
        String reply = readFromServer(socketChannel);
        clientManager.getUsersFileManager().writeToFile(reply);
    }

    private static SocketChannel connectToServer(UserInterface ui, ClientManager clientManager) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

        ui.displayConnectedToServer();

        return socketChannel;
    }
    private static void communicateWithServer(SocketChannel socketChannel,ClientManager clientManager, UserInterface ui) throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String message;
            do {
                ui.displayMessagePrompt();
                message = scanner.nextLine();

                if ("quit".equals(message)) {
                    return;
                }

            } while (!clientManager.checkCommand(message));


            ui.displaySendingMessage(message);
            writeToServer(socketChannel, message);

            String reply = readFromServer(socketChannel);
            ui.displayReply(reply);
        }
    }
    private static String setClientName(ClientManager clientManager, UserInterface ui, SocketChannel channel)
        throws IOException {
        String message = null;
        Scanner scanner = new Scanner(System.in);
        String reply = "Invalid name";
       while (reply.contains("Invalid")) {
           do {
               ui.displayNamePrompt();
               message = scanner.nextLine();
           } while (!clientManager.checkName(message));

           writeToServer(channel, message);
           reply = readFromServer(channel);
           ui.displayReply(reply);
       }
       clientManager.createClientDirectory(message);
       clientManager.createUsersFileManager(message);
       return message;
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
