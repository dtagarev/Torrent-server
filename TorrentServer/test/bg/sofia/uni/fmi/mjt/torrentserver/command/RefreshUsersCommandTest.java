package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RefreshUsersCommandTest {
    @Mock
    ServerStorage storage;
    @Mock
    SocketChannel socketChannel;
    @Mock
    Socket socket;
    @Mock
    InetAddress inetAddress;

    @InjectMocks
    RefreshUsersCommand refreshUsersCommand;

    @Test
    void testExecute() throws UnknownHostException {
        when(socketChannel.socket()).thenReturn(socket);
        when(socket.getInetAddress()).thenReturn(inetAddress);
        when(socket.getPort()).thenReturn(1234);
        when(inetAddress.toString()).thenReturn("testAddress");
        when(storage.getData()).
                thenReturn(Map.of("user1", new User("user1", socketChannel, Set.of("file1", "file2"))));

        String res = refreshUsersCommand.execute(List.of());
        assertEquals("user1-testAddress:1234\n", res);
    }

    @Test
    void testExecuteWithArguments() {
        String res = refreshUsersCommand.execute(List.of("user1"));
        assertEquals("Invalid command, No arguments needed.\n"
                + "Please use the following format: refresh-users", res);
    }
}
