package bg.sofia.uni.fmi.mjt.torrentserver.storage;

import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.Set;

public record User(String username, SocketChannel socketChannel, Set<String> files) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
            Objects.equals(socketChannel, user.socketChannel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, socketChannel);
    }

}
