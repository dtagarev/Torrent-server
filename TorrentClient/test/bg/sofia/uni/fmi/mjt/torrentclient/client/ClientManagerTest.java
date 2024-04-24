package bg.sofia.uni.fmi.mjt.torrentclient.client;

import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.Cli;
import bg.sofia.uni.fmi.mjt.torrentclient.userinterface.UserInterface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


//public class ClientManagerTest {
//    UserInterface ui = new Cli();
//    ClientManager clientManager = new ClientManager(ui);
//
//    @Test
//    public void testCheckNameTest() {
//        String name = "test";
//        String name2 = "test2";
//
//        assertTrue(clientManager.checkName(name));
//        assertTrue(clientManager.checkName(name2));
//    }
//
//    @Test
//    void testCheckNameWithInvalidCharacters() {
//        String name = "test@";
//        String name2 = "test2#";
//        String name3 = "test name";
//        String name4 = "test,-name";
//
//        assertFalse(clientManager.checkName(name));
//        assertFalse(clientManager.checkName(name2));
//        assertFalse(clientManager.checkName(name3));
//        assertFalse(clientManager.checkName(name4));
//    }
//
//}
