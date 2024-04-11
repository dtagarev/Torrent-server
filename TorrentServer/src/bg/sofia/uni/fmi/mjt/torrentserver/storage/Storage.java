package bg.sofia.uni.fmi.mjt.torrentserver.storage;

import java.nio.channels.SocketChannel;
import java.util.List;

public interface Storage {
    void registerNewUser(String username, SocketChannel socketChannel, List<String> files);

    void unregister(String username, List<String> files);

    void register(String username, List<String> args);
}
