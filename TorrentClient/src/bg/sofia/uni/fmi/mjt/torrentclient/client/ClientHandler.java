package bg.sofia.uni.fmi.mjt.torrentclient.client;

import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerConnection;
import bg.sofia.uni.fmi.mjt.torrentclient.exception.ServerConnectionException;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.ClientStorage;
import bg.sofia.uni.fmi.mjt.torrentclient.miniserver.MiniServer;
import bg.sofia.uni.fmi.mjt.torrentclient.refresher.UserRefresher;
import bg.sofia.uni.fmi.mjt.torrentclient.refresher.UsersFileManager;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.Cli;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.UserInterface;
import bg.sofia.uni.fmi.mjt.torrentclient.command.CommandChecker;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

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
    ExecutorService executor;

    public ClientHandler() {
        try {
            this.serverConnection = new ServerConnection(SERVER_HOST, SERVER_PORT, BUFFER_SIZE);
        } catch (ServerConnectionException e) {
            throw new RuntimeException(e);
        }

        Path logFilePath = Path.of(System.getProperty("user.dir") + File.separator + "clientLogs.txt");
        this.errorHandler = new ErrorHandler(logFilePath);
        this.ui = new Cli();
        executor = newVirtualThreadPerTaskExecutor();
    }

    private void initialize(String name) throws IOException {
        //this.commandChecker = new CommandChecker();
        //this.usersFileManager = new UsersFileManager();
        //clientManager.createClientDirectory(message);
        //clientManager.createUsersFileManager(message);
        String miniServerPort = null;
        try {
            String clientHost = serverConnection.getClientHost();
            InetSocketAddress miniServerAddress = new InetSocketAddress(clientHost, 0);
            ServerSocketChannel miniServerSocketChannel = ServerSocketChannel.open();
            miniServerSocketChannel.bind(miniServerAddress);

            Thread miniServerThread = new Thread(new MiniServer(miniServerSocketChannel ,storage, errorHandler));
            executor.submit(miniServerThread);
            miniServerPort = Integer.toString(miniServerSocketChannel.socket().getLocalPort());

        } catch (IOException e) {
            errorHandler.writeToLogFile(e);
            ui.displayErrorMessage("There is problem seeding files to other clients.\n" +
                    "No files will be seeded");
        }

        if(miniServerPort == null) {
            serverConnection.writeToServer("0");
            //TODO: handle zero inside the server
        }
        else {
            serverConnection.writeToServer(miniServerPort);
        }

//        UserRefresher refresher =
//                new UserRefresher(SERVER_PORT, SERVER_HOST,
//                        name,
//                        errorHandler,
//                        ui,
//                        usersFileManager);
//
//        Thread refresherThread = new Thread(refresher);
//        refresherThread.setDaemon(true);
//        executor.submit(refresherThread);
    }

    private String setClientName() throws IOException {
        String message = null;
        Scanner scanner = new Scanner(System.in);
        String reply = "Invalid name";
        while (reply.contains("Invalid")) {
            ui.displayNamePrompt();
            message = scanner.nextLine();

            if(!commandChecker.checkUsername(message)) {
                ui.displayErrorMessage("Invalid name\n"
                        + "Name should contain only lowercase letters and digits");
                reply = "Invalid name";
                continue;
            }

            reply = serverConnection.communicateWithServer(message);
            ui.displayReply(reply);
        }

        return message;
    }

    public void start() {
        try {
            String clientName = setClientName();

            initialize(clientName);
        } catch (IOException e) {
            errorHandler.writeToLogFile(e);
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}
