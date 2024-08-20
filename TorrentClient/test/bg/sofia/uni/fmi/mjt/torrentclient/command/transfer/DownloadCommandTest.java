package bg.sofia.uni.fmi.mjt.torrentclient.command.transfer;

import bg.sofia.uni.fmi.mjt.shared.exceptions.InvalidCommand;
import bg.sofia.uni.fmi.mjt.torrentclient.filetransfer.receive.FileRequest;
import bg.sofia.uni.fmi.mjt.torrentclient.refresher.UsersFileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DownloadCommandTest {

    @Mock
    private UsersFileManager usersFileManager;

    @Mock
    private BlockingQueue<FileRequest> downloadQueue;


    @InjectMocks
    private DownloadCommand downloadCommand;

    Path tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("temp-file", ".txt");
    }

    @Test
    public void testDownloadCommandWithExistingFilePahtToSave() {
        List<String> args = List.of("user1", "userPath", tempFile.toString());
        assertThrows(InvalidCommand.class, () -> downloadCommand.execute(args));
    }

    @Test
    public void testDownloadCommandWithInvalidCharacters() {
        List<String> args = List.of("use!r1", "path1$",tempFile.toString());
        assertThrows(InvalidCommand.class, () -> downloadCommand.execute(args));
    }

    @Test
    public void testDownloadCommandWithInvalidPathToSave() {
        List<String> args = List.of("user1", "userPath","path2");
        assertThrows(InvalidCommand.class, () -> downloadCommand.execute(args));
    }


}
