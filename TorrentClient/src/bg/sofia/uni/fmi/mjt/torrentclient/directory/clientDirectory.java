package bg.sofia.uni.fmi.mjt.torrentclient.directory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.exists;

public class clientDirectory implements UserDirectory {

    private Path dirPath;

    public clientDirectory() {
        Path tmpPath = Path.of(System.getProperty("user.dir") + System.lineSeparator() + "clientDirectory");


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

    @Override
    public void addFile(File file) {

    }

    @Override
    public void removeFile() {
    }

    @Override
    public void getFiles() {

    }
}
