package bg.sofia.uni.fmi.mjt.torrentclient.refresher;

import bg.sofia.uni.fmi.mjt.torrentclient.exceptions.UserNotFoundInFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersFileManagerTest {
    @TempDir
    Path tempDirectory;

    File tempFile;
    UsersFileManager usersFileManager;

    @BeforeEach
    public void setup() {
        tempFile = tempDirectory.resolve("connectedUsers.txt").toFile();
        boolean isCreated;
        try {
            isCreated = tempFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(!isCreated) {
            throw new RuntimeException("File was not created");
        }
        usersFileManager = new UsersFileManager(tempFile.toPath());
    }

    @Test
    public void testWriteToFile() {
        String message = "test";
        try {
            usersFileManager.writeToFile(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try (Reader reader = new FileReader(tempFile);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = bufferedReader.readLine();
            assertEquals(line, message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testReadFromFile() {
        String message = "test\n" + "this is just a test";
        try (Writer wr = new FileWriter(tempFile);BufferedWriter bufferedWriter = new BufferedWriter(wr)) {
            bufferedWriter.write(message);
            bufferedWriter.flush();

            String readMessage = usersFileManager.readFromFile();

            assertEquals(readMessage, message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetUserData() {
        String message = "test\n" + "this is just a test";
        try (Writer wr = new FileWriter(tempFile);BufferedWriter bufferedWriter = new BufferedWriter(wr)) {
            bufferedWriter.write(message);
            bufferedWriter.flush();

            String readMessage = usersFileManager.getUserData("test");

            assertEquals(readMessage, "test");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testGetUserDataWhenUserIsNotFound() {
        String message = "test\n" + "this is just a test";
        try (Writer wr = new FileWriter(tempFile);BufferedWriter bufferedWriter = new BufferedWriter(wr)) {
            bufferedWriter.write(message);
            bufferedWriter.flush();

            try {
                usersFileManager.getUserData("notFound");
            } catch (Exception e) {
                assertEquals(e.getClass(), UserNotFoundInFile.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
