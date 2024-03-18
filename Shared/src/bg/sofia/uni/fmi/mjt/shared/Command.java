package bg.sofia.uni.fmi.mjt.shared;

import java.util.List;

public interface Command {
    void execute(List<String> cmd);

    String toString();
}
