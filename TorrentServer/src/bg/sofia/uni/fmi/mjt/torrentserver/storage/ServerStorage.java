package bg.sofia.uni.fmi.mjt.torrentserver.storage;

import java.util.*;

public class ServerStorage implements Storage {
    private Map<String, Set<String>> data;

    public ServerStorage() {
        data = new HashMap<>();
    }

    @Override
    public void register(String username, Set<String> files) {
        if(data.containsKey(username)) {
            Set<String> tmp = data.get(username);
            tmp.addAll(files);
            data.put(username, tmp);
        } else {
            data.put(username, files);
        }
    }

    @Override
    public void unregister(String username) {
        if(!data.containsKey(username)) {
            throw new IllegalArgumentException("User does not exist");
        }
        data.remove(username);
    }

    public Map<String, Set<String>> getData() {
        return data;
    }
}
