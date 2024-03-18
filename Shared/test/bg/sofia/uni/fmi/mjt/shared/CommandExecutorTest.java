package bg.sofia.uni.fmi.mjt.shared;

import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidSymbolInCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommandExecutorTest {

    @Mock
    Command command1;

    @Mock
    Command command2;

    @Mock
    Command command3;

    @Mock
    Command command4;

    CommandExecutor commandExecutor;

    @BeforeEach
    public void setup() {
        Set<Command> commands = Set.of(command1, command2, command3, command4);
        commandExecutor = new CommandExecutor(commands);
    }

    @Test
    public void testExecuteMultipleFiles() {

//        Mockito.when(command1.toString()).thenReturn("register");
//        Mockito.when(command2.toString()).thenReturn("unregister");
//        Mockito.when(command3.toString()).thenReturn("list-files");
        Mockito.when(command4.toString()).thenReturn("download");

        String execution = "download user1 file1, file2, file3";
        List<String> args = new ArrayList<>(List.of(execution.split(" ")));
        args.removeFirst();
        commandExecutor.execute(execution);
        verify(command4, times(1)).execute(args);
        verify(command1, times(0)).execute(args);
        verify(command2, times(0)).execute(args);
        verify(command3, times(0)).execute(args);

    }

    @Test
    public void testExecuteMultipleFilesNoSpaces() {

        Mockito.when(command4.toString()).thenReturn("download");

        String execution = "download user1 file1,file2,file3";

        List<String> args = new ArrayList<>(List.of(execution.split(" ")));
        args.removeFirst();

        commandExecutor.execute(execution);
        verify(command4, times(1)).execute(args);
        verify(command1, times(0)).execute(args);
        verify(command2, times(0)).execute(args);
        verify(command3, times(0)).execute(args);

    }

    @Test
    public void testInvalidCommand() {

        String invalidCommand = "invalid";

        assertThrows(InvalidCommand.class, () -> commandExecutor.execute(invalidCommand));
        verify(command1, times(0)).execute(List.of());
        verify(command2, times(0)).execute(List.of());

    }

    @Test
    public void testInvalidSymbolInCommand() {

        assertThrows(InvalidSymbolInCommand.class, () -> commandExecutor.execute("download user1 file1!"));

        verify(command1, times(0)).execute(List.of("user1", "file1!"));
        verify(command2, times(0)).execute(List.of("user1", "file1!"));

    }

}