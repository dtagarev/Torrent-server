package bg.sofia.uni.fmi.mjt.torrentserver.storage;

import java.util.List;

public interface Storage {
    void register(String username, List<String> files);

    void unregister(String username);
}
