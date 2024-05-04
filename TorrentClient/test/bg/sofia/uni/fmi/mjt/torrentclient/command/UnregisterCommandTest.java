package bg.sofia.uni.fmi.mjt.torrentclient.command;

import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.command.server.UnregisterCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class UnregisterCommandTest {

    UnregisterCommand unregisterCommand = new UnregisterCommand(null);

    @Test
    void testUnregisterCommand() {
        try(MockedStatic<UserDirectory> userDirMock = Mockito.mockStatic(UserDirectory.class)) {
            userDirMock.when(() -> UserDirectory.isAFile(any())).thenReturn(true);
            List<String> args = List.of("user1", "file1,file2");
            assertEquals("unregister user1 file1,file2",unregisterCommand.execute(args));
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