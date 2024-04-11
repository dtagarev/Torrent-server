package bg.sofia.uni.fmi.mjt.shared.command;

import java.util.List;

public interface Command {
    String execute(List<String> cmd);

    @Override
    String toString();
}
