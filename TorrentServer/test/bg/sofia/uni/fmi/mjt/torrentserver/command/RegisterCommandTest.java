package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegisterCommandTest {

    ServerStorage storage = new ServerStorage();
    @BeforeEach
    void setUp() {
        storage = new ServerStorage();
    }
    @Test
    void testExecute() {
        RegisterCommand registerCommand = new RegisterCommand(storage);
        registerCommand.execute(List.of("user1", "file1", "file2"));

        assertTrue(storage.getData().get("user1").contains("file1"));
    }
}
