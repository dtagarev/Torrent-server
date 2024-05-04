package bg.sofia.uni.fmi.mjt.torrentclient.command;

import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.command.server.RegisterCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RegisterCommandTest {

    RegisterCommand registerCommand = new RegisterCommand();

    @Test
    void testRegisterCommand() {
        try(MockedStatic<UserDirectory> userDirMock = Mockito.mockStatic(UserDirectory.class)) {
            userDirMock.when(() -> UserDirectory.isAFile(any())).thenReturn(true);
            List<String> args = List.of("user1", "file1,file2");
            assertEquals("register user1 file1,file2",registerCommand.execute(args));
        }
    }

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
