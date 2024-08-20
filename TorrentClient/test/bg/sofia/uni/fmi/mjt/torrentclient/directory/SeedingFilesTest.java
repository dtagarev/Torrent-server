package bg.sofia.uni.fmi.mjt.torrentclient.directory;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeedingFilesTest {
    @TempDir
    private Path tempDirectory;

    private File tempFile;
    private SeedingFiles seedingFiles = new SeedingFiles();

    @BeforeEach
    public void setup() {
        tempFile = tempDirectory.resolve("SeedingFilesTest.txt").toFile();
        boolean isCreated;
        try {
            isCreated = tempFile.createNewFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(!isCreated) {
            throw new RuntimeException("File was not created");
        }
    }

    @Test
    public void testContainsFile() {
        seedingFiles.addFilePath(tempFile.getAbsolutePath());
        assertTrue(seedingFiles.containsFilePath(tempFile.getAbsolutePath()));
    }

    @Test
    public void testAddFileWithNoFile() {
        seedingFiles.addFilePath("noFile");
        assertTrue(seedingFiles.containsFilePath("noFile"));
    }

    @Test
    public void testRemoveFile() {
        seedingFiles.addFilePath(tempFile.getAbsolutePath());

        seedingFiles.removeFilePath(tempFile.getAbsolutePath());
        assertFalse(seedingFiles.containsFilePath(tempFile.getAbsolutePath()));
    }
}