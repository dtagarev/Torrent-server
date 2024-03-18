package bg.sofia.uni.fmi.mjt.torrentserver.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerStorage implements Storage {
    private Map<String, List<String>> data;

    public ServerStorage() {
        data = new HashMap<>();
    }

    @Override
    public void register(String username, List<String> files) {
        data.put(username, files);
    }

    @Override
    public void unregister(String username) {
        data.remove(username);
    }

    public Map<String, List<String>> getData() {
        return data;
    }
}
