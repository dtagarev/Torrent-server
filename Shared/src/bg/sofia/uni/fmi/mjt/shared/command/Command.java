package bg.sofia.uni.fmi.mjt.shared.command;

import java.util.List;

public interface Command {
    String execute(List<String> cmd);

    int getNumberOfArguments();

    int getFileArgumentIndex();

    @Override
    String toString();
}
