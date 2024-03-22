package bg.sofia.uni.fmi.mjt.torrentserver.storage;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServerStorage implements Storage {
    private Map<String, Set<String>> data;

    public ServerStorage() {
        data = new HashMap<>();
    }

    @Override
    public synchronized void register(String username, List<String> files) {
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
    public synchronized void unregister(String username) {
        if(!data.containsKey(username)) {
            throw new IllegalArgumentException("User does not exist");
        }
        data.remove(username);
    }

    public synchronized Map<String, Set<String>> getData() {
        return data;
    }
}
