package bg.sofia.uni.fmi.mjt.shared;

public interface Command {
    void execute(String cmd);

    String toString();
}
