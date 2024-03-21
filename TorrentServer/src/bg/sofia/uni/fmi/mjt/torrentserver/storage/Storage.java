package bg.sofia.uni.fmi.mjt.torrentserver.storage;

import java.util.List;
import java.util.Set;

public interface Storage {
    void register(String username, List<String> files);

    void unregister(String username);
}
