package bg.sofia.uni.fmi.mjt.torrentserver.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerStorageTest {
    ServerStorage storage = new ServerStorage();

    String user1 = "user1";
    String user2 = "user2";
    String user3 = "user3";
    String file1 = "file1";
    String file2 = "file2";
    String file3 = "file3";

    @BeforeEach
    void setUp() {
        storage = new ServerStorage();
    }

    @Test
    void testRegister() {
        storage.register(user1, Set.of(file1, file2));
        storage.register(user2, Set.of(file2, file3));
        storage.register(user3, Set.of(file1, file3));

        assertTrue(storage.getData().get(user1).contains(file1));
    }
}
