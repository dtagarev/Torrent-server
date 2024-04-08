package bg.sofia.uni.fmi.mjt.torrentclient.directory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.isRegularFile;

public  class  ClientStorage  implements UserDirectory {
    //TODO: should it be synchronized?

    private Path dirPath;

    public ClientStorage(Path clientStoragePath) {
        //dirPath = Path.of(System.getProperty("user.dir") + System.lineSeparator() + "ClientStorage");
        dirPath = clientStoragePath;
    }

    @Override
    public synchronized boolean containsFile(String filename) {
        return exists(dirPath.resolve(filename));
    }

    @Override
    public synchronized void addFile(Path file) {
        if(!containsFile(file.getFileName().toString())) {
            try {
                Files.copy(file, dirPath.resolve(file.getFileName()));
            } catch (IOException e) {
                //TODO: should fix it , and the function if the received file is not in a path format
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public synchronized List<Path> getSeedingFiles() {
        List<Path> lst = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            for (Path fileOrSubDir : stream) {
                if (isRegularFile(fileOrSubDir)) {
                    lst.add(fileOrSubDir);
                }
            }
        } catch (IOException | DirectoryIteratorException e) {
            throw new RuntimeException(e);
        }

        return lst;
    }

    @Override
    public synchronized Path getFile(String filename) throws FileNotFoundException {
        if (!containsFile(filename)) {
            throw new FileNotFoundException("File not found");
        }
        return dirPath.resolve(filename);
    }

}
