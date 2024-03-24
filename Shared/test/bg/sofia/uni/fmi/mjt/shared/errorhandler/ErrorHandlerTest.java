package bg.sofia.uni.fmi.mjt.shared.errorhandler;

import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.shared.exceptions.EmptyCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorHandlerTest {
    @TempDir
    Path tempDir;

    @Test
    void testWriteToLogFile() throws IOException {
        Path tempFile = Files.createTempFile(tempDir, "test", ".txt");
        ErrorHandler errorHandler = new ErrorHandler(tempFile);

        RuntimeException exception = new EmptyCommand("Test exception");
        errorHandler.writeToLogFile(exception);

        String fileContent = Files.readString(tempFile);
        assertTrue(fileContent.contains("Test exception"));
    }

    @Test
    void testWriteMessageToLogFile() throws IOException {
        Path tempFile = Files.createTempFile(tempDir, "test", ".txt");
        ErrorHandler errorHandler = new ErrorHandler(tempFile);

        RuntimeException exception = new EmptyCommand("Test exception");
        errorHandler.writeToLogFile(exception, "Test message");

        String fileContent = Files.readString(tempFile);
        assertTrue(fileContent.contains("Test message"));
    }
}