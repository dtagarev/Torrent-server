package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.shared.errorhanler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UnregisterCommandTest {
    ServerStorage storage;

    @Mock
    ErrorHandler errorHandler;

    @InjectMocks
    UnregisterCommand unregisterCommand;

    @BeforeEach
    void setUp() {
        storage = new ServerStorage();
        storage.addNewUser("user1", null, List.of("file1", "file2"));
        storage.addNewUser("user2", null, List.of("file2", "file3"));
        storage.addNewUser("user3", null, List.of("file1", "file3"));
    }

    @Test
    void testExecute() {
        UnregisterCommand unregCommand = new UnregisterCommand(storage, errorHandler);
        unregCommand.execute(List.of("user1", "file1"));

        Set<String> user1Files = storage.getData().get("user1").files();

        assertTrue(user1Files.contains("file2") && !user1Files.contains("file1"));
        assertTrue(storage.getData().containsKey("user1"));
    }

    @Test
    void testExecuteUserDoesNotExist() {
        unregisterCommand = new UnregisterCommand(storage, errorHandler);
        String str = unregisterCommand.execute(List.of("user4", "file1"));

        assertEquals("User does not exist", str);
        verify(errorHandler).writeToLogFile(any());
    }

    @Test
    void testExecuteNotEnoughArguments() {
        unregisterCommand = new UnregisterCommand(storage, errorHandler);
        String str = unregisterCommand.execute(List.of());
        String str2 = unregisterCommand.execute(List.of("user1"));

        assertTrue(str.contains("Invalid command. Not enough arguments"));
        assertTrue(str2.contains("Invalid command. Not enough arguments"));
    }
}
