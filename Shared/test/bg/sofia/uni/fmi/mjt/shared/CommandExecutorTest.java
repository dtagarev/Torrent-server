package bg.sofia.uni.fmi.mjt.shared;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class CommandExecutorTest {

    @Mock
    Command command;

    @InjectMocks
    Set<Command> commands;

    @InjectMocks
    CommandExecutor commandExecutorMock;

    @Test
    void testCommandContainsCorrectSymbols() {

        commandExecutorMock.execute("test");
    }

}
