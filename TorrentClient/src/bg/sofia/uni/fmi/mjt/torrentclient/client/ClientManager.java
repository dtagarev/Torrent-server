package bg.sofia.uni.fmi.mjt.torrentclient.client;
import bg.sofia.uni.fmi.mjt.shared.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.shared.exceptions.EmptyCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidSymbolInCommand;
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
        this.storage = new ClientStorage();
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

    public void startServices() {


    }
}
