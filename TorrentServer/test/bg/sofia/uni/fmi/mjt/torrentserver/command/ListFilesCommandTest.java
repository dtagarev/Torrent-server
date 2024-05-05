package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListFilesCommandTest {

    Map<String, User> mapStorage = new HashMap<>();

    @Mock
    ServerStorage storage;

    @BeforeEach
    void setUp() {
        mapStorage.put("user1", new User("user1", null, 1234,  Set.of("file1", "file2")));
        mapStorage.put("user2", new User("user2", null, 0,  Set.of("file3", "file4")));
    }

    @Test
    void testExecute() {
        when(storage.getData()).thenReturn(mapStorage);

        ListFilesCommand listFilesCommand = new ListFilesCommand(storage);
        String str = listFilesCommand.execute(List.of());

        System.out.println(str);
        assertTrue(str.equals("user1 : [file2, file1]")
                || str.equals("user1 : [file1, file2]"));
    }

    @Test
    void testExecuteReturnsInvalidCommandString() {
        ListFilesCommand listFilesCommand = new ListFilesCommand(storage);
        String str = listFilesCommand.execute(List.of("arg1"));

        assertTrue(str.equals("Invalid command. No arguments needed."));
    }

}
