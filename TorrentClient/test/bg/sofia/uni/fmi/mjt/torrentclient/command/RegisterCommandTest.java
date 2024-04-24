package bg.sofia.uni.fmi.mjt.torrentclient.command;

import bg.sofia.uni.fmi.mjt.torrentclient.directory.UserDirectory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisterCommandTest {
    @Mock
    UserDirectory userDirectory;

    @InjectMocks
    RegisterCommand registerCommand = new RegisterCommand();

    @Test
    void testRegisterCommand() {
        when(UserDirectory.isAFile(any())).thenReturn(true);

        List<String> args = List.of("user1", "file1,file2");
        assertEquals("register user1 file1,file2",registerCommand.execute(args));
    }
}
