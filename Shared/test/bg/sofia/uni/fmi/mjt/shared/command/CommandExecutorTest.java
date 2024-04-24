package bg.sofia.uni.fmi.mjt.shared.command;

import bg.sofia.uni.fmi.mjt.shared.exceptions.EmptyCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;

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
    public void testExecute() throws InvalidCommand, EmptyCommand {

        Mockito.when(command1.toString()).thenReturn("register");
        Mockito.when(command2.toString()).thenReturn("unregister");
        Mockito.when(command3.toString()).thenReturn("list-files");
        Mockito.when(command4.toString()).thenReturn("download");

        String execution1 = "register";
        String execution2 = "unregister";
        String execution3 = "list-files";
        String execution4 = "download";

        List<String> args1 = new ArrayList<>(List.of(execution1.split(" ")));
        args1.removeFirst();
        List<String> args2 = new ArrayList<>(List.of(execution2.split(" ")));
        args2.removeFirst();
        List<String> args3 = new ArrayList<>(List.of(execution3.split(" ")));
        args3.removeFirst();
        List<String> args4 = new ArrayList<>(List.of(execution4.split(" ")));
        args4.removeFirst();

        commandExecutor.execute(execution1);
        commandExecutor.execute(execution2);
        commandExecutor.execute(execution3);
        commandExecutor.execute(execution4);

        verify(command1, times(1)).execute(args1);
        verify(command2, times(1)).execute(args2);
        verify(command3, times(1)).execute(args3);
        verify(command4, times(1)).execute(args4);
    }
    @Test
    public void testExecuteMultipleFiles() throws InvalidCommand, EmptyCommand {

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
    public void testExecuteMultipleFilesNoSpaces() throws InvalidCommand, EmptyCommand {

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

        assertThrows(InvalidCommand.class, () -> commandExecutor.execute("invalid"));
        assertThrows(InvalidCommand.class, () -> commandExecutor.execute("invalid user1 file1"));

        verify(command1, times(0)).execute(List.of());
        verify(command2, times(0)).execute(List.of());
        verify(command3, times(0)).execute(List.of());
        verify(command4, times(0)).execute(List.of());

        verify(command1, times(0)).execute(List.of("user1", "file1"));
        verify(command2, times(0)).execute(List.of("user1", "file1"));
        verify(command3, times(0)).execute(List.of("user1", "file1"));
        verify(command4, times(0)).execute(List.of("user1", "file1"));
    }

    @Test
    public void testEmptyCommand() {

        assertThrows(EmptyCommand.class, () -> commandExecutor.execute(""));
        assertThrows(EmptyCommand.class, () -> commandExecutor.execute(" "));

        verify(command1, times(0)).execute(List.of());
        verify(command2, times(0)).execute(List.of());
        verify(command3, times(0)).execute(List.of());
        verify(command4, times(0)).execute(List.of());

    }

}