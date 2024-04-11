package bg.sofia.uni.fmi.mjt.torrentserver.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.channels.SocketChannel;
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

    SocketChannel socket1;
    SocketChannel socket2;
    SocketChannel socket3;

    @BeforeEach
    void setUp() {
        storage = new ServerStorage();
        socket1 = Mockito.mock(SocketChannel.class);
        socket2 = Mockito.mock(SocketChannel.class);
        socket3 = Mockito.mock(SocketChannel.class);
    }

    @Test
    void testAddNewUser() {
        storage.addNewUser(user1, socket1, List.of());
        storage.addNewUser(user2, socket2, List.of(file2, file3));

        assertTrue(storage.getData().containsKey(user1));
        assertTrue(storage.getData().containsKey(user2));
        assertEquals(2, storage.getData().size());

        assertTrue(storage.getData().get(user1).files().isEmpty());
        assertEquals(2, storage.getData().get(user2).files().size());

        assertEquals(socket1,storage.getData().get(user1).socketChannel());
        assertEquals(socket2,storage.getData().get(user2).socketChannel());
    }
    @Test
    void testRegister() {
        storage.addNewUser(user1, socket1, List.of());
        storage.addNewUser(user2, socket2, List.of());
        storage.addNewUser(user3, socket3, List.of());

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

        storage.addNewUser(user1, socket1, List.of());
        storage.addNewUser(user2, socket2, List.of());
        storage.addNewUser(user3, socket3, List.of());

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
        storage.addNewUser(user1, socket1, List.of(file1, file2, file3));
        storage.addNewUser(user2, socket2, List.of(file2, file3));
        storage.addNewUser(user3, socket3, List.of(file1, file3));

        storage.unregister(user1, List.of(file1, file2));
        storage.unregister(user2, List.of(file2));
        storage.unregister(user3, List.of(file1, file3));

        Set<String> user1Files = storage.getData().get(user1).files();
        Set<String> user2Files = storage.getData().get(user2).files();

        assertTrue(user1Files.contains(file3) && user1Files.size() == 1);
        assertTrue(user2Files.contains(file3) && user2Files.size() == 1);
        assertTrue(storage.getData().get(user3).files().isEmpty());
    }

    @Test
    void testUnregisterThrowsException() {
        storage.addNewUser(user1, socket1, List.of(file1, file2));
        storage.addNewUser(user2, socket2, List.of(file2, file3));
        storage.addNewUser(user3, socket3, List.of(file1, file3));

        assertThrows(IllegalArgumentException.class, () -> storage.unregister("user4", List.of(file1)));
    }

    @Test
    void testRemoveClientRemoveAllClients() {
        storage.addNewUser(user1, socket1, List.of(file1, file2));
        storage.addNewUser(user2, socket2, List.of(file2, file3));
        storage.addNewUser(user3, socket3, List.of(file1, file3));

        storage.removeUser(user1);
        storage.removeUser(user2);
        storage.removeUser(user3);

        assertTrue(storage.getData().isEmpty());
    }

    @Test
    void testRemoveClientRemoveSomeClients() {
        storage.addNewUser(user1, socket1, List.of(file1, file2));
        storage.addNewUser(user2, socket2, List.of(file2, file3));
        storage.addNewUser(user3, socket3, List.of(file1, file3));

        storage.removeUser(user1);
        storage.removeUser(user3);

        assertEquals(1, storage.getData().size());
        assertTrue(storage.getData().containsKey(user2));
    }

    @Test
    void testRemoveUserThrowsException() {
        storage.addNewUser(user1, socket1, List.of(file1, file2));

        assertThrows(IllegalArgumentException.class, () -> storage.removeUser("user4"));
    }
}
