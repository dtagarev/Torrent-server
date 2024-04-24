package bg.sofia.uni.fmi.mjt.torrentclient.command;

public class SyntaxChecker {
    public static boolean checkUsername(String name) {
        return name.matches("[a-z0-9]+");
    }

    public static boolean checkCommandContainsCorrectSymbols(String cmd) {
        return cmd.matches("[a-zA-Z0-9. \\\\/,-]+");
    }
}

