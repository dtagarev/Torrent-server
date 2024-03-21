package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnregisterCommandTest {
    ServerStorage storage;
    @BeforeEach
    void setUp() {
        storage = new ServerStorage();
        storage.register("user1", List.of("file1", "file2"));
        storage.register("user2", List.of("file2", "file3"));
        storage.register("user3", List.of("file1", "file3"));
    }

    @Test
    void testExecute() {
        UnregisterCommand unregisterCommand = new UnregisterCommand(storage);
        unregisterCommand.execute(List.of("user1"));

        assertFalse(storage.getData().containsKey("user1"));
    }
}
