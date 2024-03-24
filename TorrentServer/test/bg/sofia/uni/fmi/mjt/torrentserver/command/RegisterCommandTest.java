package bg.sofia.uni.fmi.mjt.torrentserver.command;

import bg.sofia.uni.fmi.mjt.torrentserver.storage.ServerStorage;
import bg.sofia.uni.fmi.mjt.torrentserver.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegisterCommandTest {

    ServerStorage storage = new ServerStorage();
    @BeforeEach
    void setUp() {
        storage = new ServerStorage();
        storage.register("user1", List.of("file1", "file2"));
    }
    @Test
    void testExecute() {
        RegisterCommand registerCommand = new RegisterCommand(storage);
        String str = registerCommand.execute(List.of("user1", "file1", "file2"));

        System.out.println(str);
        assertTrue(str.equals("Server storage updated with new user and files."));
        assertTrue(storage.getData().get("user1").contains("file1"));
    }

    @Test
    void testExecuteUserDoesNotExist() {
        RegisterCommand registerCommand = new RegisterCommand(storage);
        String str = registerCommand.execute(List.of("user4", "file1", "file2"));

        assertTrue(str.equals("There is no user with this username."));
    }

    @Test
    void testExecuteNotEnoughArguments() {
        RegisterCommand registerCommand = new RegisterCommand(storage);
        String str = registerCommand.execute(List.of("user1"));

        assertTrue(str.equals("Invalid command. Not enough arguments."));
    }

    @Test
    void testExecuteTooManyArguments() {
        RegisterCommand registerCommand = new RegisterCommand(storage);
        String answer = "Invalid command format. Too many arguments.\n" +
                "Please use the following format: register <username> <file1,file2,fileN>";

        String str1 = registerCommand.execute(List.of("user1", "file1", "file2", "file3"));
        String str2 = registerCommand.execute(List.of("user1", "file1,file2,", "file3"));

        assertTrue(str1.equals(answer));
        assertTrue(str2.equals(answer));
    }
}
