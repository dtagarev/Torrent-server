package bg.sofia.uni.fmi.mjt.torrentclient.directory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientStorageTest {

    @TempDir
    Path tempDirectory;

    @TempDir
    Path otherDirectory;

    ClientStorage storage;

    @BeforeEach
    public void setup() {
        try {
            Files.createTempDirectory(tempDirectory, "ClientStorage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        storage = new ClientStorage(tempDirectory);
    }

    @Test
    public void testContainsFileNoneExistent() {
        try {
            Files.createFile(otherDirectory.resolve("file.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertFalse(storage.containsFile("file.txt"));
    }

    @Test
    public void testContainsFile() {
        try {
            Files.createFile(tempDirectory.resolve("file.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertTrue(storage.containsFile("file.txt"));
    }

    @Test
    public void testAddFile() {
        Path file;
        try {
            file = Files.createFile(otherDirectory.resolve("file.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        storage.addFile(file);
        assertTrue(storage.containsFile("file.txt"));
    }

    @Test
    public void testAddFileAlreadyExists() {
        Path file;
        try {
            file = Files.createFile(tempDirectory.resolve("file.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        storage.addFile(file);
        assertTrue(storage.containsFile("file.txt"));
    }

    @Test
    public void testGetSeedingFiles() {
        Path file1;
        Path file2;
        try {
            file1 = Files.createFile(tempDirectory.resolve("file1.txt"));
            file2 = Files.createFile(tempDirectory.resolve("file2.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Path> seedingFiles = storage.getSeedingFiles();

        assertTrue(seedingFiles.contains(file1));
        assertTrue(seedingFiles.contains(file2));
    }

    @Test
    public void testGetSeedingFilesEmpty() {
        List<Path> seedingFiles = storage.getSeedingFiles();

        assertTrue(seedingFiles.isEmpty());
    }

    @Test
    public void testGetFile() {
        Path file;
        try {
            file = Files.createFile(tempDirectory.resolve("file.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Path testFile;
        try {
            testFile = storage.getFile("file.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(file, testFile);
    }

    @Test
    public void testGetFileThrowsException() {
        assertThrows(FileNotFoundException.class, () -> storage.getFile("file.txt"));
    }
}
