package bg.sofia.uni.fmi.mjt.torrentclient.client;

import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.shared.exceptions.EmptyCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidSymbolInCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.command.SyntaxChecker;
import bg.sofia.uni.fmi.mjt.torrentclient.command.server.ListFilesCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.command.server.RegisterCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.command.server.UnregisterCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.command.transfer.DownloadCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.command.ui.HelpCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerConnection;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.SeedingFiles;
import bg.sofia.uni.fmi.mjt.torrentclient.exceptions.ServerConnectionException;
import bg.sofia.uni.fmi.mjt.torrentclient.executors.GlobalCommandExecutor;
import bg.sofia.uni.fmi.mjt.torrentclient.filetransfer.send.MiniServer;
import bg.sofia.uni.fmi.mjt.torrentclient.refresher.UserRefresher;
import bg.sofia.uni.fmi.mjt.torrentclient.refresher.UsersFileManager;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.Cli;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.UserInterface;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.Set;
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

    private ServerConnection serverConnection;
    private ErrorHandler errorHandler;
    private SeedingFiles storage;
    private UserInterface ui;
    private UsersFileManager usersFileManager;
    private ExecutorService executor;
    private GlobalCommandExecutor commandExecutor;

    public ClientHandler() {
        try {
            this.serverConnection = new ServerConnection(SERVER_HOST, SERVER_PORT, BUFFER_SIZE);
        } catch (ServerConnectionException e) {
            throw new RuntimeException(e);
        }
        storage = new SeedingFiles();
        Path logFilePath = Path.of(System.getProperty("user.dir") + File.separator + "clientLogs.txt");
        this.errorHandler = new ErrorHandler(logFilePath);
        this.ui = new Cli();
        executor = newVirtualThreadPerTaskExecutor();
    }

    private void initializeCommandExecutor() {
        commandExecutor = new GlobalCommandExecutor(
                Set.of(new RegisterCommand(serverConnection),
                        new UnregisterCommand(serverConnection),
                        new ListFilesCommand(serverConnection),
                        new HelpCommand(),
                        new DownloadCommand(usersFileManager)
                ));
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
            initializeCommandExecutor();
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

            if(!SyntaxChecker.checkUsername(message)) {
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

    public void start() {
        try {
            setClientName();
        } catch (IOException e) {
            errorHandler.writeToLogFile(e);
            throw new RuntimeException("There is a problem with the network communication", e);
        }

        Scanner in = new Scanner(System.in);
        String message = null;

        do {
            ui.displayMessagePrompt();
            message = in.nextLine();
            try {
                final String result = commandExecutor.execute(message);
                ui.displayReply(result);
            } catch (EmptyCommand | InvalidSymbolInCommand | InvalidCommand e) {
                errorHandler.writeToLogFile(e);
                ui.displayErrorMessage(e.getMessage());
            }
        } while (!message.equals("quit"));
            //TODO: gasi


    }

    private void shutDown() {
        //TODO: close all connections
    }
}
