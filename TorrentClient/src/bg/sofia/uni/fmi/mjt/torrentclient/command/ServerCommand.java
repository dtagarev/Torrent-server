package bg.sofia.uni.fmi.mjt.torrentclient.command;


public record ServerCommand(String name, int argumentCount, int fileArgumentIndex) {
    @Override
    public String toString() {
        return name;
    }
}
