package bg.sofia.uni.fmi.mjt.torrentclient.client;

import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.shared.exceptions.EmptyCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidSymbolInCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerConnection;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.SeedingFiles;
import bg.sofia.uni.fmi.mjt.torrentclient.exceptions.ServerConnectionException;
import bg.sofia.uni.fmi.mjt.torrentclient.miniserver.MiniServer;
import bg.sofia.uni.fmi.mjt.torrentclient.refresher.UserRefresher;
import bg.sofia.uni.fmi.mjt.torrentclient.refresher.UsersFileManager;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.Cli;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.UserInterface;
import bg.sofia.uni.fmi.mjt.torrentclient.command.CommandChecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

public class ClientHandler {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;

    //TODO: fix tests
    //TODO: storage fix
    //TODO: handle commands
    // download files
    // transfer help command here

    ServerConnection serverConnection;
    ErrorHandler errorHandler;
    SeedingFiles storage;
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
        //TODO: initialize storage corecctly
        storage = new SeedingFiles();
        //TODO: command checker
        commandChecker = new CommandChecker(storage);
        Path logFilePath = Path.of(System.getProperty("user.dir") + File.separator + "clientLogs.txt");
        this.errorHandler = new ErrorHandler(logFilePath);
        this.ui = new Cli();
        executor = newVirtualThreadPerTaskExecutor();
    }

    private void initializeUsersFileManager(String clientName) throws IOException {
        Path usersFilePath = Path.of(System.getProperty("user.dir") + File.separator + clientName + "ActiveUsers.txt");

        String errorMessage = "Cannot create active users file.\n"
                + "Further error-free usage of the program is not guaranteed";

        if(!Files.exists(usersFilePath)) {
            Files.createFile(usersFilePath);
        }

        usersFileManager = new UsersFileManager(usersFilePath);
    }

    private void initializeUserRefresher(String name) {
        UserRefresher refresher = new UserRefresher(serverConnection,
                usersFileManager, errorHandler, ui);

        Thread refresherThread = new Thread(refresher);
        refresherThread.setDaemon(true);
        executor.submit(refresherThread);
    }

    private String initializeMiniServer() {
        try {
            String clientHost = serverConnection.getClientHost();
            InetSocketAddress miniServerAddress = new InetSocketAddress(clientHost, 0);
            ServerSocketChannel miniServerSocketChannel = ServerSocketChannel.open();
            miniServerSocketChannel.bind(miniServerAddress);

            Thread miniServerThread = new Thread(new MiniServer(miniServerSocketChannel ,storage, errorHandler));
            miniServerThread.setDaemon(true);

            executor.submit(miniServerThread);

            return Integer.toString(miniServerSocketChannel.socket().getLocalPort());

        } catch (IOException e) {
            errorHandler.writeToLogFile(e);
            ui.displayErrorMessage("There is problem seeding files to other clients.\n" +
                    "No files will be seeded");
            return "0";
        }
    }

    private void initializeAll(String name) throws IOException {

        String miniServerPort = initializeMiniServer();
        //TODO: handle zero inside the server
        serverConnection.writeToServer(miniServerPort);

        try {
            initializeUsersFileManager(name);
            initializeUserRefresher(name);
        } catch (IOException e) {
            errorHandler.writeToLogFile(e);
            ui.displayErrorMessage("Cannot create active users file.\n"
                    + "Further error-free usage of the program is not guaranteed");
        }
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

        initializeAll(message);
        return message;
    }

    private boolean checkCommand(String message) {
        try {
            commandChecker.check(message);
            return true;
        } catch (FileNotFoundException | IllegalArgumentException | EmptyCommand | InvalidCommand |
                 InvalidSymbolInCommand e) {
            errorHandler.writeToLogFile(e);
            ui.displayErrorMessage(e.getMessage());
        }
        return false;
    }

    public void start() {
        try {
            String clientName = setClientName();
            Scanner in = new Scanner(System.in);
            String message = null;

            while (true) {
                do {
                    ui.displayMessagePrompt();
                    message = in.nextLine();
                } while(!checkCommand(message));

                if("quit".equals(message)) {
                    shutDown();
                    return;
                }

                if(message.contains("download")) {

                } else {
                    String reply = serverConnection.communicateWithServer(message);
                    ui.displayReply(reply);
                }
            }
        } catch (IOException e) {
            errorHandler.writeToLogFile(e);
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }

    private void shutDown() {
    }
}
