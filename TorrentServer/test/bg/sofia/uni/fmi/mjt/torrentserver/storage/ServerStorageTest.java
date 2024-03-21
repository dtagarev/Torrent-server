package bg.sofia.uni.fmi.mjt.torrentserver.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
        storage.register(user1, List.of(file1, file2));
        storage.register(user2, List.of(file2, file3));
        storage.register(user3, List.of(file1, file3));

        assertTrue(storage.getData().get(user1).contains(file1));
        assertTrue(storage.getData().get(user1).contains(file2));
        assertTrue(storage.getData().get(user2).contains(file2));
        assertTrue(storage.getData().get(user2).contains(file3));
        assertTrue(storage.getData().get(user3).contains(file1));
        assertTrue(storage.getData().get(user3).contains(file3));
    }
    @Test
    void testRegisterWithRepeatingFiles() {

        storage.register(user1, List.of(file1));
        storage.register(user1, List.of(file1));
        storage.register(user1, List.of(file2));
        storage.register(user2, List.of(file2));
        storage.register(user2, List.of(file2, file3));
        storage.register(user3, List.of(file1));
        storage.register(user3, List.of(file3));

        assertTrue(storage.getData().get(user1).contains(file1));
        assertTrue(storage.getData().get(user1).contains(file2));
        assertTrue(storage.getData().get(user2).contains(file2));
        assertTrue(storage.getData().get(user2).contains(file3));
        assertTrue(storage.getData().get(user3).contains(file1));
        assertTrue(storage.getData().get(user3).contains(file3));

    }
    @Test
    void testUnregister() {
        storage.register(user1, List.of(file1, file2));
        storage.register(user2, List.of(file2, file3));
        storage.register(user3, List.of(file1, file3));

        storage.unregister(user1);
        storage.unregister(user2);
        storage.unregister(user3);

        assertTrue(storage.getData().isEmpty());
    }

    @Test
    void testUnregisterThrowsException() {
        storage.register(user1, List.of(file1, file2));
        storage.register(user2, List.of(file2, file3));
        storage.register(user3, List.of(file1, file3));

        storage.unregister(user1);
        storage.unregister(user2);
        storage.unregister(user3);

        assertThrows(IllegalArgumentException.class, () -> storage.unregister(user1));
    }
}
