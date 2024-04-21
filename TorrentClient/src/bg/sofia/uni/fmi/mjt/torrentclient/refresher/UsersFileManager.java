package bg.sofia.uni.fmi.mjt.torrentclient.refresher;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class UsersFileManager {
    Path usersFile;

    public UsersFileManager(Path filepath) {
        usersFile = filepath;
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
        byte[] bytes = Files.readAllBytes(usersFile);
        return new String(bytes, StandardCharsets.UTF_8);
        //return Files.readString(usersFile, StandardCharsets.UTF_8);
    }
}