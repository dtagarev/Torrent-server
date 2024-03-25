package bg.sofia.uni.fmi.mjt.torrentclient.client;
import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.ClientStorage;

import java.io.File;
import java.nio.file.Path;

public class ClientManager {
    ErrorHandler errorHandler;
    ClientStorage storage;

    public ClientManager() {

        Path logFilePath = Path.of(System.getProperty("user.dir") + File.separator + "clientLogs.txt");
        this.errorHandler = new ErrorHandler(logFilePath);
    }

    public String enterName(String name) {
        //TODO: name validation
        return name;
    }

    public String enterCommand(String s) {
        //TODO: command validation
        return s;
    }

    public void setupNewClient() {
        //TODO: function should throw exceptions
        //TODO: setup new client
        //storage, reader thread, and other stuff, basically like constructor
        System.out.println("New client setup");
    }
}
