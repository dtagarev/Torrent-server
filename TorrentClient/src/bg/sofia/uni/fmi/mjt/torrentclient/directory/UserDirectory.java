package bg.sofia.uni.fmi.mjt.torrentclient.directory;

import java.nio.file.Files;
import java.nio.file.Path;

public interface UserDirectory {
    boolean containsFilePath(String filePath);
    void removeFilePath(String file);
    void addFilePath(String file);
    Object getFileKey(String file);
    static boolean isAFile(String file) {
        Path filePath = Path.of(file).toAbsolutePath().normalize();
        return Files.isRegularFile(filePath);
    }
}
