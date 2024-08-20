package bg.sofia.uni.fmi.mjt.torrentserver.storage;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServerStorage implements Storage {
    private final Map<String, User> data;

    public ServerStorage() {
        data = new HashMap<>();
    }

    public void addNewUser(String username, SocketChannel socketChannel, List<String> files) {
        Set<String> userFiles;
        if (data.containsKey(username)) {
            userFiles = data.get(username).files();
            userFiles.addAll(files);
        } else {
            userFiles = new HashSet<>(files);
        }
        data.put(username, new User(username, socketChannel, null, userFiles));
    }

    public void setClientServerPort(String username, Integer port) {
        if (!data.containsKey(username)) {
            throw new IllegalArgumentException("User does not exist");
        }
        User oldUser = data.get(username);
        data.put(username, new User(oldUser.username(), oldUser.socketChannel(), port, oldUser.files()));
    }

    @Override
    public synchronized void unregister(String username, List<String> files) {
        if (!data.containsKey(username)) {
            throw new IllegalArgumentException("User does not exist");
        }

        files.forEach(data.get(username).files()::remove);
    }

    @Override
    public void register(String username, List<String> files) {
        data.get(username).files().addAll(files);
    }

    public synchronized void removeUser(String username) {
        if (!data.containsKey(username)) {
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
