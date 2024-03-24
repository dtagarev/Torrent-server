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
        storage.register("user1", List.of("file1", "file2"));
        storage.register("user2", List.of("file2", "file3"));
        storage.register("user3", List.of("file1", "file3"));
    }

    @Test
    void testExecute() {
        UnregisterCommand unregCommand = new UnregisterCommand(storage, errorHandler);
        unregCommand.execute(List.of("user1"));

        assertFalse(storage.getData().containsKey("user1"));
    }

    @Test
    void testExecuteUserDoesNotExist() {
        unregisterCommand = new UnregisterCommand(storage, errorHandler);
        String str = unregisterCommand.execute(List.of("user4"));

        assertTrue(str.equals("User does not exist"));
        verify(errorHandler).writeToLogFile(any());
    }

    @Test
    void testExecuteNotEnoughArguments() {
        unregisterCommand = new UnregisterCommand(storage, errorHandler);
        String str = unregisterCommand.execute(List.of());

        assertTrue(str.equals("Invalid command. Not enough arguments."));
    }
}
