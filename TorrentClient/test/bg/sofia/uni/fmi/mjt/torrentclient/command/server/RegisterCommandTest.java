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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisterCommandTest {

    @Mock
    private ServerCommunicator serverCommunicator;

    @Mock
    private UserDirectory storage;

    RegisterCommand registerCommand = new RegisterCommand(serverCommunicator, storage);

    //@Test
    //void testExecute() {
    //    when(UserDirectory.isAFile(any())).thenReturn(true);
    //}

    @Test
    void testRegisterCommandWithInvalidUsername() {
        try(MockedStatic<UserDirectory> userDirMock = Mockito.mockStatic(UserDirectory.class)) {
            userDirMock.when(() -> UserDirectory.isAFile(any())).thenReturn(true);
            List<String> args = List.of("user1@", "file1,file2");
            assertThrows(InvalidCommand.class, () -> registerCommand.execute(args));
        }
    }

    @Test
    void testRegisterCommandWithInvalidFile() {
        try(MockedStatic<UserDirectory> userDirMock = Mockito.mockStatic(UserDirectory.class)) {
            userDirMock.when(() -> UserDirectory.isAFile(any())).thenReturn(false);
            List<String> args = List.of("user1", "file1,file2");
            assertThrows(InvalidCommand.class, () -> registerCommand.execute(args));
        }
    }

    @Test
    void testRegisterCommandWithInvalidNumberOfArguments() {
        try(MockedStatic<UserDirectory> userDirMock = Mockito.mockStatic(UserDirectory.class)) {
            userDirMock.when(() -> UserDirectory.isAFile(any())).thenReturn(true);
            List<String> args = List.of("user1");
            assertThrows(InvalidCommand.class, () -> registerCommand.execute(args));

            List<String> args2 = List.of("user1", "file1", "file2");
            assertThrows(InvalidCommand.class, () -> registerCommand.execute(args2));
        }
    }

    @Test
    void testRegisterCommandWithInvalidCharacters() {
        try(MockedStatic<UserDirectory> userDirMock = Mockito.mockStatic(UserDirectory.class)) {
            userDirMock.when(() -> UserDirectory.isAFile(any())).thenReturn(true);
            List<String> args = List.of("user1", "file1,file2$");
            assertThrows(InvalidCommand.class, () -> registerCommand.execute(args));
        }
    }
}
