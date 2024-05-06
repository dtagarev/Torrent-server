package bg.sofia.uni.fmi.mjt.torrentclient.command.server;

import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.connection.ServerCommunicator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListFilesCommandTest {

    @Mock
    private ServerCommunicator serverCommunicator;

    @Test
    public void testExecute() throws IOException {
        ListFilesCommand listFilesCommand = new ListFilesCommand(serverCommunicator);
        when(serverCommunicator.communicateWithServer("list-files")).thenReturn("returned server message");

        assertTrue(listFilesCommand.execute(List.of()).equals("returned server message"));
    }

    @Test
    public void testExecuteThrowsInvalidCommandNumberOfCharacters() {
        ListFilesCommand listFilesCommand = new ListFilesCommand(serverCommunicator);

        assertThrows(InvalidCommand.class, () -> listFilesCommand.execute(List.of("a")));
    }
}
