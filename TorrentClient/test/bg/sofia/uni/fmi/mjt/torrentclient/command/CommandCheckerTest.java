package bg.sofia.uni.fmi.mjt.torrentclient.command;

import bg.sofia.uni.fmi.mjt.shared.exceptions.EmptyCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidSymbolInCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CommandCheckerTest {
    @Mock
    UserDirectory userDirectory;
    @InjectMocks
    CommandChecker commandChecker;


    @Test
    public void testCheckWithRegister() throws FileNotFoundException {
        when(userDirectory.containsFile("file1")).thenReturn(true);
        when(userDirectory.containsFile("file2")).thenReturn(true);
        String command = "register user file1,file2";

        assertEquals(command, commandChecker.check(command));
    }
    @Test
    public void testCheckWithUnregister() throws FileNotFoundException {
        when(userDirectory.containsFile("file1")).thenReturn(true);
        when(userDirectory.containsFile("file2")).thenReturn(true);
        String command = "unregister user file1,file2";

        assertEquals(command, commandChecker.check(command));
    }
    @Test
    public void testCheckWithListFiles() throws FileNotFoundException {
        String command = "list-files";
        assertEquals(command, commandChecker.check(command));
    }
    @Test
    public void  testCheckWithHelp() throws FileNotFoundException {
        String command = "help";
        assertEquals(command, commandChecker.check(command));
    }

    @Test
    public void testCheckWithRegisterWithInvalidFile() {
        when(userDirectory.containsFile("file1")).thenReturn(true);
        when(userDirectory.containsFile("file2")).thenReturn(false);

        String command = "register user file1,file2";
        String command2 = "register user file2";

        assertThrows(FileNotFoundException.class, () -> commandChecker.check(command));
        assertThrows(FileNotFoundException.class, () -> commandChecker.check(command2));
    }

    @Test
    public void testCheckWithInvalidNumberOfArguments() {
        String command = "register file1";
        String command2 = "register user file1 file2";
        String command3 = "register user file1, file2";


        assertThrows(InvalidCommand.class, () -> commandChecker.check(command));
        assertThrows(InvalidCommand.class, () -> commandChecker.check(command2));
        assertThrows(InvalidCommand.class, () -> commandChecker.check(command3));
    }

    @Test
    public void testCheckWithInvalidCommand() {
        String command = "invalidCommand";
        String command2 = "register-user user file1,file2";

        assertThrows(InvalidCommand.class, () -> commandChecker.check(command));
        assertThrows(InvalidCommand.class, () -> commandChecker.check(command2));
    }

    @Test
    public void testCheckWithEmptyCommand() {
        String command = "";

        assertThrows(EmptyCommand.class, () -> commandChecker.check(command));
    }
    @Test
    public void testCheckWithBlankCommand() {
        String command = " ";

        assertThrows(EmptyCommand.class, () -> commandChecker.check(command));
    }
    @Test
    public void testCheckWithInvalidSymbolInCommand() {
        String command = "register user file1,file2!";
        String command2 = "reg!ister user file1,file2";
        String command3 = "register u^ser file1,file2";
        String command4 = "list_files";

        assertThrows(InvalidSymbolInCommand.class, () -> commandChecker.check(command));
        assertThrows(InvalidSymbolInCommand.class, () -> commandChecker.check(command2));
        assertThrows(InvalidSymbolInCommand.class, () -> commandChecker.check(command3));
        assertThrows(InvalidSymbolInCommand.class, () -> commandChecker.check(command4));
    }
}
