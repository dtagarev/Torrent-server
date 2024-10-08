package bg.sofia.uni.fmi.mjt.torrentclient.filetransfer.receive;

import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerCommunicator;
import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerConnection;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.UserInterface;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.BlockingQueue;

public class FileReceiver implements Runnable {

    private final BlockingQueue<FileRequest> downloadQueue;
    private final ErrorHandler errorHandler;
    private final UserInterface ui;

    private final ServerCommunicator mainServerConnection;
    private final String username;


    private final UserDirectory userDirectory;

    public FileReceiver(BlockingQueue<FileRequest> downloadQueue,
                        ErrorHandler errorHandler, UserInterface ui,
                        ServerConnection serverConnection, String name, UserDirectory userDirectory) {
        this.downloadQueue = downloadQueue;
        this.errorHandler = errorHandler;
        this.ui = ui;
        this.mainServerConnection = serverConnection;
        this.username = name;
        this.userDirectory = userDirectory;

    }

    private SocketChannel connectToServer(String host, int port) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(host, port));

        return socketChannel;
    }

    @Override
    public void run() {
        try {
            while (true) {
                FileRequest fileRequest = downloadQueue.take();
                SocketChannel socketChannel = connectToServer(fileRequest.host(), fileRequest.port());

                try (FileChannel fileChannel = FileChannel.open(
                        fileRequest.to(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {

                    ui.displayReply("FileReceiver: File " + fileRequest.from() + " is going to be received");

                    fileChannel.transferFrom(socketChannel, 0, Long.MAX_VALUE);


                } catch (IOException e) {
                    errorHandler.writeToLogFile(e);
                    Files.deleteIfExists(fileRequest.to());
                    ui.displayErrorMessage("File " + fileRequest.from() + " was not received");
                }

                mainServerConnection.communicateWithServer("register " + username + " " + fileRequest.to());
                userDirectory.addFilePath(fileRequest.to().toString());

                ui.displayReply("File " + fileRequest.from() + " was received");
            }
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
        }
    }
}
