package bg.sofia.uni.fmi.mjt.torrentclient.directory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.exists;

public  class  ClientStorage  implements UserDirectory {

    private Path dirPath;

    public ClientStorage() {
        dirPath = Path.of(System.getProperty("user.dir") + System.lineSeparator() + "ClientStorage");

        if (!exists(dirPath)) {
            try {
                createDirectory(dirPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean containsFile(String filename) {
        //TODO: implement
        // 100% incorrect
        for(Path file : dirPath) {
            if(file.getFileName().toString().equals(filename)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getSeedingFiles() {

        //TODO: implement
        // 100% incorrect
        for(Path file : dirPath) {
            System.out.println(file.getFileName().toString());
        }
        return null;
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
