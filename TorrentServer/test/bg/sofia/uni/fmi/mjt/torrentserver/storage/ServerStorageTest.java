package bg.sofia.uni.fmi.mjt.torrentserver.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        assertTrue(storage.getData().get(user1).files().contains(file1));
        assertTrue(storage.getData().get(user1).files().contains(file2));
        assertTrue(storage.getData().get(user2).files().contains(file2));
        assertTrue(storage.getData().get(user2).files().contains(file3));
        assertTrue(storage.getData().get(user3).files().contains(file1));
        assertTrue(storage.getData().get(user3).files().contains(file3));
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

        assertTrue(storage.getData().get(user1).files().contains(file1));
        assertTrue(storage.getData().get(user1).files().contains(file2));
        assertTrue(storage.getData().get(user2).files().contains(file2));
        assertTrue(storage.getData().get(user2).files().contains(file3));
        assertTrue(storage.getData().get(user3).files().contains(file1));
        assertTrue(storage.getData().get(user3).files().contains(file3));

    }
    @Test
    void testUnregister() {
        storage.register(user1, List.of(file1, file2, file3));
        storage.register(user2, List.of(file2, file3));

        storage.unregister(user1, List.of(file1, file2));
        storage.unregister(user2, List.of(file2));

        Set<String> user1Files = storage.getData().get(user1).files();
        Set<String> user2Files = storage.getData().get(user2).files();

        assertTrue(user1Files.contains(file3) && user1Files.size() == 1);
        assertTrue(user2Files.contains(file3) && user2Files.size() == 1);
    }

    @Test
    void testUnregisterThrowsException() {
        storage.register(user1, List.of(file1, file2));
        storage.register(user2, List.of(file2, file3));
        storage.register(user3, List.of(file1, file3));

        assertThrows(IllegalArgumentException.class, () -> storage.unregister("user4", List.of(file1)));
    }

    @Test
    void testRemoveClientRemoveAllClients() {
        storage.register(user1, List.of(file1, file2));
        storage.register(user2, List.of(file2, file3));
        storage.register(user3, List.of(file1, file3));

        storage.removeClient(user1);
        storage.removeClient(user2);
        storage.removeClient(user3);

        assertTrue(storage.getData().isEmpty());
    }

    @Test
    void testRemoveClientRemoveSomeClients() {
        storage.register(user1, List.of(file1, file2));
        storage.register(user2, List.of(file2, file3));
        storage.register(user3, List.of(file1, file3));

        storage.removeClient(user1);
        storage.removeClient(user3);

        assertEquals(1, storage.getData().size());
        assertTrue(storage.getData().containsKey(user2));
    }

    @Test
    void testRemoveClientThrowsException() {
        storage.register(user1, List.of(file1, file2));

        assertThrows(IllegalArgumentException.class, () -> storage.removeClient("user4"));
    }
}
