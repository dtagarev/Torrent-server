package bg.sofia.uni.fmi.mjt.torrentserver.storage;


import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServerStorage implements Storage {
    private Map<String, User> data;

    public ServerStorage() {
        data = new HashMap<>();
    }

    @Override
    public synchronized void registerNewUser(String username, SocketChannel socketChannel, List<String> files) {
        Set<String> userFiles;
        if(data.containsKey(username)) {
            userFiles = data.get(username).files();
            userFiles.addAll(files);
        } else {
            userFiles = new HashSet<>(files);
        }
        data.put(username ,new User(username, socketChannel, userFiles));
    }


    @Override
    public synchronized void unregister(String username, List<String> files) {
        if(!data.containsKey(username)) {
            throw new IllegalArgumentException("User does not exist");
        }

        files.forEach(data.get(username).files()::remove);
    }

    @Override
    public void register(String username, List<String> files) {
        Set<String> userFiles;
        if(data.containsKey(username)) {
            userFiles = data.get(username).files();
            userFiles.addAll(files);
        } else {
            userFiles = new HashSet<>(files);
        }
        //data.put(username ,new User(username, socketChannel, userFiles));
        data.get(username).files().addAll(files);
    }

    public synchronized void removeClient(String username) {
        if(!data.containsKey(username)) {
            throw new IllegalArgumentException("User does not exist");
        }

        data.remove(username);
    }

    public synchronized Map<String, User> getData() {
        return data;
    }

    public synchronized boolean containsUser(String username) {
        return data.containsKey(username);
    }
}
