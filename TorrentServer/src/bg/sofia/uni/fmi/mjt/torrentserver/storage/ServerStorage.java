package bg.sofia.uni.fmi.mjt.torrentserver.storage;

import java.util.*;
import java.util.stream.Collectors;

public class ServerStorage implements Storage {
    private Map<String, Set<String>> data;

    public ServerStorage() {
        data = new HashMap<>();
    }

    @Override
    public void register(String username, List<String> files) {
        Set<String> userFiles;
        if(data.containsKey(username)) {
            userFiles = data.get(username);
            userFiles.addAll(files);
        } else {
            userFiles = new HashSet<>(files);
        }
        data.put(username, userFiles);
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
