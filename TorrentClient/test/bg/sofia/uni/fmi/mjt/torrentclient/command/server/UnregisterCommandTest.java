package bg.sofia.uni.fmi.mjt.torrentclient.command.server;

import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerCommunicator;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UnregisterCommandTest {

    private UnregisterCommand unregisterCommand = new UnregisterCommand(null, null);


    @Mock
    private ServerCommunicator serverCommunicator;

    @Mock
    private UserDirectory storage;

    @Test
    void testExecute() throws InvalidCommand, IOException {
        when(serverCommunicator.communicateWithServer(any())).thenReturn("test");

        try(MockedStatic<UserDirectory> userDirMock = Mockito.mockStatic(UserDirectory.class)) {
            userDirMock.when(() -> UserDirectory.isAFile(any())).thenReturn(true);
            List<String> args = List.of("user1", "file1,file2");

            UnregisterCommand otherCmd = new UnregisterCommand(serverCommunicator, storage);
            assertEquals("test",otherCmd.execute(args));

            verify(storage).removeFilePath("file1,file2");
        }
    }

    @Test
    void testUnregisterCommandWithInvalidUsername() {
        try(MockedStatic<UserDirectory> userDirMock = Mockito.mockStatic(UserDirectory.class)) {
            userDirMock.when(() -> UserDirectory.isAFile(any())).thenReturn(true);
            List<String> args = List.of("user1@", "file1,file2");
            assertThrows(InvalidCommand.class, () -> unregisterCommand.execute(args));
        }
    }

    @Test
    void testUnregisterCommandWithInvalidFile() {
        try(MockedStatic<UserDirectory> userDirMock = Mockito.mockStatic(UserDirectory.class)) {
            userDirMock.when(() -> UserDirectory.isAFile(any())).thenReturn(false);
            List<String> args = List.of("user1", "file1,file2");
            assertThrows(InvalidCommand.class, () -> unregisterCommand.execute(args));
        }
    }

    @Test
    void testUnregisterCommandWithInvalidNumberOfArguments() {
        try(MockedStatic<UserDirectory> userDirMock = Mockito.mockStatic(UserDirectory.class)) {
            userDirMock.when(() -> UserDirectory.isAFile(any())).thenReturn(true);
            List<String> args = List.of("user1");
            assertThrows(InvalidCommand.class, () -> unregisterCommand.execute(args));

            List<String> args2 = List.of("user1", "file1", "file2");
            assertThrows(InvalidCommand.class, () -> unregisterCommand.execute(args2));
        }
    }

    @Test
    void testUnregisterCommandWithInvalidCharacters() {
        try(MockedStatic<UserDirectory> userDirMock = Mockito.mockStatic(UserDirectory.class)) {
            userDirMock.when(() -> UserDirectory.isAFile(any())).thenReturn(true);
            List<String> args = List.of("user1", "file1,file2$");
            assertThrows(InvalidCommand.class, () -> unregisterCommand.execute(args));
        }
    }
}