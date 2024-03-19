package bg.sofia.uni.fmi.mjt.shared.command;

import java.util.List;

public interface Command {
    void execute(List<String> cmd);

    @Override
    String toString();
}
