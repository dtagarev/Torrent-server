package bg.sofia.uni.fmi.mjt.torrentserver.storage;

import java.util.Set;

public interface Storage {
    void register(String username, Set<String> files);

    void unregister(String username);
}
