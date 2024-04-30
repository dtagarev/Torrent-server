package bg.sofia.uni.fmi.mjt.torrentclient.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SyntaxCheckerTest {

    @Test
    void testCheckUsername() {
        assertTrue(SyntaxChecker.checkUsername("user1"));
    }

    @Test
    void testCheckUsernameWithInvalidUsername() {
        assertFalse(SyntaxChecker.checkUsername("user1@"));
        assertFalse(SyntaxChecker.checkUsername("user_1"));
        assertFalse(SyntaxChecker.checkUsername("user-1"));
    }

    @Test
    void testCheckCommandContainsCorrectSymbols() {
        assertTrue(SyntaxChecker.checkCommandContainsCorrectSymbols("register user1 file1,file2"));
        assertTrue(SyntaxChecker.checkCommandContainsCorrectSymbols("list-files"));
        assertTrue(SyntaxChecker.checkCommandContainsCorrectSymbols("download ./some/random/path"));
        assertTrue(SyntaxChecker.checkCommandContainsCorrectSymbols("download .\\some\\random\\windows\\path"));
    }

    @Test
    void testCheckCommandContainsCorrectSymbolsInvalidSymbols() {
        assertFalse(SyntaxChecker.checkCommandContainsCorrectSymbols("register user1 file1,file2!"));
        assertFalse(SyntaxChecker.checkCommandContainsCorrectSymbols("list-files!@"));
        assertFalse(SyntaxChecker.checkCommandContainsCorrectSymbols("download ./some/random/path!"));
    }
}
