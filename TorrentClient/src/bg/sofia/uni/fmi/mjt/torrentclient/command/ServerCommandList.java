package bg.sofia.uni.fmi.mjt.torrentclient.command;

public enum ServerCommandList {
    REGISTER("register"),
    UNREGISTER("unregister"),
    LIST_FILES("list-files"),

    HELP("help");

    private final String command;

    ServerCommandList(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static boolean contains(String command) {
        for (ServerCommandList c : ServerCommandList.values()) {
            if (c.getCommand().equals(command)) {
                return true;
            }
        }
        return false;
    }
}
