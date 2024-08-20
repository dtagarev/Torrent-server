package bg.sofia.uni.fmi.mjt.torrentserver.storage;

import java.util.List;

public interface Storage {

    void unregister(String username, List<String> files);

    void register(String username, List<String> args);
}
