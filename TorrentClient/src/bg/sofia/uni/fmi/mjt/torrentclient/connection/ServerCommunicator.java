package bg.sofia.uni.fmi.mjt.torrentclient.connection;

import java.io.IOException;

public interface ServerCommunicator {
    String communicateWithServer(String message) throws IOException;
}
