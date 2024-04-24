package bg.sofia.uni.fmi.mjt.torrentclient.command;

public enum AvailableCommands {

    register("register", 3, 2),
    unregister("unregister",3, 2),
    list_files("list-files",1, -1),
    help("help",1, -1),
    download("download", 4, 2),
    quit("quit", 1, -1);

    private final String commandName;
    private final int commandArgs;
    private final int fileArgsIdx;

    AvailableCommands(String commandName, int commandArgs, int fileArgsIdx) {
        this.commandName = commandName;
        this.commandArgs = commandArgs;
        this.fileArgsIdx = fileArgsIdx;
    }

    public static boolean contains(String first) {
        for (AvailableCommands command : AvailableCommands.values()) {
            if (command.commandName.equals(first)) {
                return true;
            }
        }
        return false;
    }

    public String getCommandName() {
        return commandName;
    }

    public int getCommandArgs() {
        return commandArgs;
    }

    public int getFileArgsIdx() {
        return fileArgsIdx;
    }
}
