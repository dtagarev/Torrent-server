package bg.sofia.uni.fmi.mjt.torrentclient.command.ui;

import bg.sofia.uni.fmi.mjt.shared.command.Command;

import java.util.List;

public class QuitCommand implements Command {

    @Override
    public String execute(List<String> list) {
        return "Quiting the application";
    }

    @Override
    public String toString() {
        return "quit";
    }
}
