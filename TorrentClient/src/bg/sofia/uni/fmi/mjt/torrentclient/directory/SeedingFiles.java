package bg.sofia.uni.fmi.mjt.torrentclient.directory;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SeedingFiles implements UserDirectory {
    private final Map<Path, Object> files;

    public SeedingFiles() {
        this.files = new HashMap<>();
    }

    public boolean containsFilePath(String filePath) {
        Path file = Path.of(filePath).toAbsolutePath().normalize();
        return files.containsKey(file);
    }

    public void removeFilePath(String file) {
        Path filePath = Path.of(file).toAbsolutePath().normalize();
        files.remove(filePath);
    }

    public void addFilePath(String filePath) {
        Path file = Path.of(filePath).toAbsolutePath().normalize();
        files.put(file, new Object());
    }

}
