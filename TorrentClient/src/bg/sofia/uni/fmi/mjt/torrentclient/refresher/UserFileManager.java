package bg.sofia.uni.fmi.mjt.torrentclient.refresher;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class UserFileManager {
    Path usersFile;

    public UserFileManager(String username) {
        usersFile = Path.of(System.getProperty("user.dir")
        + System.lineSeparator()
        + "connectedUsers" + username + ".txt");
    }

    public synchronized void createUserFile() throws IOException {
        if(!Files.exists(usersFile)) {
            Files.createFile(usersFile);
        }
    }

    public synchronized  void  writeToFile(String message) throws IOException {
        if(!Files.exists(usersFile)) {
            throw new FileNotFoundException("File does not exist");
        }
        FileOutputStream fos = new FileOutputStream(usersFile.toString(), false);
        fos.write(message.getBytes());
        fos.close();
    }
    public synchronized String readFromFile() throws IOException {
        if(!Files.exists(usersFile)) {
            throw new FileNotFoundException("File does not exist");
        }
        return Files.readString(usersFile, StandardCharsets.UTF_8);
    }
}
