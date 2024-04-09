package bg.sofia.uni.fmi.mjt.torrentclient.client;
import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.torrentclient.command.CommandChecker;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.ClientStorage;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.UserInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public class ClientManager {
    ErrorHandler errorHandler;
    ClientStorage storage;

    CommandChecker commandChecker;
    UserInterface ui;

    public ClientManager(UserInterface ui) {
        this.ui = ui;
        Path logFilePath = Path.of(System.getProperty("user.dir") + File.separator + "clientLogs.txt");
        this.errorHandler = new ErrorHandler(logFilePath);
        this.commandChecker = new CommandChecker(storage);

    }

    public boolean checkName(String name) {
        return name.matches("[a-z0-9]+");
    }


    public boolean checkCommand(String message) {
        try {
            commandChecker.check(message);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void createClientDirectory(String clientName) {
        if(clientName == null) {
            errorHandler.writeToLogFile(new IllegalArgumentException("Client name cannot be null"));
            ui.displayErrorMessage("Client name cannot be null");
        }
        Path clientStoragePath = Path.of(System.getProperty("user.dir") + File.separator + clientName + "Directory");
        this.storage = new ClientStorage(clientStoragePath);
    }
}
