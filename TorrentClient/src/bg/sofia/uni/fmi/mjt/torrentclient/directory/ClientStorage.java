package bg.sofia.uni.fmi.mjt.torrentclient.directory;

import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.exists;

public class ClientStorage implements UserDirectory {

    private Path dirPath;

    public ClientStorage() {
        Path tmpPath = Path.of(System.getProperty("user.dir") + System.lineSeparator() + "ClientStorage");


        if (!exists(tmpPath)) {
            try {
                createDirectory(tmpPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        dirPath = tmpPath;
    }
    @Override
    public Path getFile(String filename) {
        for(Path file : dirPath) {
            if(file.getFileName().toString().equals(filename)) {
                return file;
            }
        }
        return null;
    }

}
