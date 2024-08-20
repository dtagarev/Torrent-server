package bg.sofia.uni.fmi.mjt.torrentclient.executors;

import bg.sofia.uni.fmi.mjt.shared.command.Command;
import bg.sofia.uni.fmi.mjt.shared.exceptions.EmptyCommand;
import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientCommandExecutorTest {

    @Mock
    private Command command;

    @Test
    public void testExecuteWithEmptyCommand() {
        Set<Command> commands = new HashSet<>();
        ClientCommandExecutor executor = new ClientCommandExecutor(commands);

        assertThrows(EmptyCommand.class, () -> executor.execute(""));
        assertThrows(EmptyCommand.class, () -> executor.execute(" "));
    }

    @Test
    public void testExecuteWithInvalidCommand() {
        Set<Command> commands = new HashSet<>();
        ClientCommandExecutor executor = new ClientCommandExecutor(commands);

        assertThrows(InvalidCommand.class, () -> executor.execute("invalidCommand"));
    }

    @Test
    public void testExecuteWithValidCommand() {
        Set<Command> commands = new HashSet<>();
        commands.add(command);

        when(command.toString()).thenReturn("validCommand");
        when(command.execute(anyList())).thenReturn("Command executed");

        ClientCommandExecutor executor = new ClientCommandExecutor(commands);

        executor.execute("validCommand arg1 arg2");

        verify(command, times(1)).execute(anyList());
    }
}
