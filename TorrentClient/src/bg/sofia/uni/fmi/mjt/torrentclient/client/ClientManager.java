package bg.sofia.uni.fmi.mjt.torrentclient.client;
import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.shared.exceptions.EmptyCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidSymbolInCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.command.CommandChecker;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.ClientStorage;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;
import bg.sofia.uni.fmi.mjt.torrentclient.refresher.UsersFileManager;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.UserInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClientManager {
    ErrorHandler errorHandler;
    ClientStorage storage;

    CommandChecker commandChecker;
    UserInterface ui;

    UsersFileManager usersFileManager;

    public ClientManager(UserInterface ui) {
        this.ui = ui;
        Path logFilePath = Path.of(System.getProperty("user.dir") + File.separator + "clientLogs.txt");
        this.errorHandler = new ErrorHandler(logFilePath);

    }



    public boolean checkCommand(String message) {
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

    public void createClientDirectory(String clientName) {
        if(clientName == null) {
            errorHandler.writeToLogFile(new IllegalArgumentException("Client name cannot be null"));
            ui.displayErrorMessage("Client name cannot be null");
        }
        Path clientStoragePath = Path.of(System.getProperty("user.dir") + File.separator + clientName + "Directory");

        createDirectory(clientStoragePath);
        this.storage = new ClientStorage(clientStoragePath);
        this.commandChecker = new CommandChecker(storage);
    }

    private void createDirectory(Path clientStoragePath) {
        if(!Files.isDirectory(clientStoragePath)) {
            try {
                Files.createDirectory(clientStoragePath);
            } catch (IOException e) {
                errorHandler.writeToLogFile(e);
                ui.displayErrorMessage("Cannot create clients directory directory.\n"
                    + "Further error-free usage of the program is not guaranteed");
            }
        }
    }

    public void createUsersFileManager(String clientName) {
        Path usersFilePath = Path.of(System.getProperty("user.dir") + File.separator + clientName + "ActiveUsers.txt");

        String errorMessage = "Cannot create active users file.\n"
            + "Further error-free usage of the program is not guaranteed";

        if(!Files.exists(usersFilePath)) {
            try {
                Files.createFile(usersFilePath);
            } catch (IOException e) {
                errorHandler.writeToLogFile(e);
                ui.displayErrorMessage(errorMessage);
            }

        }
        this.usersFileManager = new UsersFileManager(usersFilePath);
    }

    public UsersFileManager getUsersFileManager() {
        return this.usersFileManager;
    }

    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    public UserDirectory getStorage() {
        return this.storage;
    }

}
